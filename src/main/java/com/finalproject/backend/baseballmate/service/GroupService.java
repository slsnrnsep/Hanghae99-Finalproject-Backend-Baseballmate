package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupApplication;
import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupApplicationRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.HotGroupResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupApplicationRepository groupApplicationRepository;

    // 모임 전체 조회(등록 순)
    public List<AllGroupResponseDto> getAllGroups() {
        List<Group> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
        List<AllGroupResponseDto> allGroupResponseDtoList = new ArrayList<>();
        for (int i=0; i<groupList.size(); i++) {
            Group group = groupList.get(i);

            Long groupId = group.getGroupId();
            String title = group.getTitle();
            String createdUsername = group.getCreatedUsername();
            int peopleLimit = group.getPeopleLimit();
            int canApplyNum = group.getCanApplyNum();
            double hotPercent = group.getHotPercent();
            String stadium = group.getStadium();
            String groupDate = group.getGroupDate();
            String filePath = group.getFilePath();
            String selectTeam = group.getSelectTeam();
            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath,selectTeam,dday);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }
        return allGroupResponseDtoList;
    }

    // 구단별 모임 조회(필터링)
    public List<AllGroupResponseDto> showGroupsByTeam(String selectedTeam, Pageable pageable) {
//        PageRequest
        Page<Group> grouppage = groupRepository.findBySelectTeam(selectedTeam,pageable);
        List<AllGroupResponseDto> allGroupResponseDtoList = new ArrayList<>();
        List<Group> groupList=grouppage.getContent();
        for(int i=0; i<groupList.size(); i++) {
            Group group = groupList.get(i);

            Long groupId = group.getGroupId();
            String title = group.getTitle();
            String createdUsername = group.getCreatedUsername();
            int peopleLimit = group.getPeopleLimit();
            int canApplyNum = group.getCanApplyNum();
            double hotPercent = group.getHotPercent();
            String stadium = group.getStadium();
            String groupDate = group.getGroupDate();
            String filePath = group.getFilePath();
            String selectTeam = group.getSelectTeam();
            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();

            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath, selectTeam,dday);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }

        return allGroupResponseDtoList;
    }

    // 핫한 모임 조회(hotPercent순) - 메인 페이지용
    public List<HotGroupResponseDto> getHotGroups() {
        List<Group> hotGroupList = groupRepository.findTop5ByOrderByHotPercentDesc();
        List<HotGroupResponseDto> hotGroupResponseDtoList = new ArrayList<>();

        for(int i=0; i< hotGroupList.size(); i++) {
            Group group = hotGroupList.get(i);

            Long groupId = group.getGroupId();
            String createdUsername = group.getCreatedUsername();
            String title = group.getTitle();
            int peopleLimit = group.getPeopleLimit();
            int canApplyNum = group.getCanApplyNum();
            double hotPercent = group.getHotPercent();
            String stadium = group.getStadium();
            String groupDate = group.getGroupDate();
            String filePath =group.getFilePath();
            String selectTeam = group.getSelectTeam();

            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();

            HotGroupResponseDto hotGroupResponseDto =
                    new HotGroupResponseDto(groupId, createdUsername, title, peopleLimit, canApplyNum, hotPercent, stadium, groupDate,filePath,selectTeam,dday);

            hotGroupResponseDtoList.add(hotGroupResponseDto);
        }
        return hotGroupResponseDtoList;
    }

    // 모임 생성
    @Transactional
    public Group createGroup(GroupRequestDto requestDto, User loginedUser) {
        Group Group = new Group(requestDto, loginedUser);
        groupRepository.save(Group);
        return Group;
    }

    // 모임 상세 조회
    public GroupDetailResponseDto getGroupDetail(Long id) {
        // 모임 entity에서 해당 모임에 대한 모든 정보 빼오기
        Group group = groupRepository.findByGroupId(id);
        List<Map<String, String>> appliedUsers = new ArrayList<>();

        // 참여자 정보 리스트 만들기
        for(int i=0; i<group.getGroupApplications().size(); i++) {
            GroupApplication groupApplication = group.getGroupApplications().get(i);
            String appliedUserInx = groupApplication.getAppliedUser().getId().toString();
            String appliedUserId = groupApplication.getAppliedUser().getUserid();
            String appliedUsername = groupApplication.getAppliedUser().getUsername();
            String appliedUserImage = groupApplication.getAppliedUser().getPicture();

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("UserImage", appliedUserImage);
            userInfo.put("Username", appliedUsername);
            userInfo.put("UserId", appliedUserId);
            userInfo.put("UserInx", appliedUserInx);

            appliedUsers.add(i, userInfo);
        }

        Long groupId = group.getGroupId();
        String createdUserName = group.getCreatedUsername();
        String title = group.getTitle();
        String content = group.getContent();
        int peopleLimit = group.getPeopleLimit();
        int nowAppliedNum = group.getNowAppliedNum();
        int canApplyNum = group.getCanApplyNum();
        double hotPercent = group.getHotPercent();
        String stadium = group.getStadium();
        String groupDate = group.getGroupDate();
        String filePath = group.getFilePath();
        List<GroupComment> groupcommentList = group.getGroupCommentList();
        List<Map<String, String>> appliedUserInfo = appliedUsers;

        // 디데이 계산
        int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
        int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
        LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
        Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
        String dday = countingday.toString();

        GroupDetailResponseDto groupdetailResponseDto =
                new GroupDetailResponseDto(groupId, createdUserName, title, content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, stadium , groupDate, filePath, dday, appliedUserInfo, groupcommentList);

        return groupdetailResponseDto;
    }


    // 모임 게시글 수정하기
    public void updateGroup(Long groupId, GroupRequestDto requestDto, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Group group = groupRepository.findByGroupId(groupId);
        if(group != null) {
            createdUserId = group.getCreatedUser().getUserid();

            if(!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
            group.updateGroup(requestDto);
            groupRepository.save(group);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    //모임 게시글 삭제하기
    public void deleteGroup(Long groupId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Group group = groupRepository.findByGroupId(groupId);
        if(group != null) {
            createdUserId = group.getCreatedUser().getUserid();

            if(!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            groupRepository.deleteById(groupId);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // 모임 참여하기
    @Transactional
    public void applyGroup(User appliedUser, Group appliedGroup) {
        List<User> canceledUserList = appliedGroup.getCanceledUser();

        // 참가 신청 이력 조회
        GroupApplication groupApplication1 = groupApplicationRepository.findByAppliedGroupAndAppliedUser(appliedGroup, appliedUser);
        if (groupApplication1 != null) {
            throw new IllegalArgumentException("참가 신청 이력이 존재합니다.");
        } else {
            if (groupApplication1 == null && canceledUserList.contains(appliedUser)) {
                throw new IllegalArgumentException("재참가는 불가합니다.");
            } else {
                GroupApplication groupApplication = new GroupApplication(appliedUser, appliedGroup);
                groupApplicationRepository.save(groupApplication);
                // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
                // 현재 참여 신청한 인원 1 증가
                int nowAppliedNum = groupApplication.getAppliedGroup().getNowAppliedNum();
                int updatedAppliedNum = nowAppliedNum + 1;
                groupApplication.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                // 현재 참여 신청 가능한 인원 1 감소
                int nowCanApplyNum = groupApplication.getAppliedGroup().getCanApplyNum();
                int updatedCanApplyNum = nowCanApplyNum - 1;
                groupApplication.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                // 인기도 값 수정
                int peopleLimit = groupApplication.getAppliedGroup().getPeopleLimit();
                double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                groupApplication.getAppliedGroup().setHotPercent(updatedHotPercent);
            }

        }
    }

    // 모임 취소하기
    // 1. 그룹에서 해당 그룹의 groupapplication list를 불러오기
    // 2. 거기에서 유저 아이디를 찾고, 로그인 한 유저 아이디와 일치하는지 확인하기
    // 3. 일치하면 해당 groupapplication을 삭제하기
    // 4. 그룹 내의 취소 리스트에 id값 추가하기

    @Transactional
    public void cancelApplication(Long groupId, UserDetailsImpl userDetails) {
        Group group = groupRepository.findByGroupId(groupId);
        List<GroupApplication> groupApplicationList = group.getGroupApplications();
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        for(int i=0; i<groupApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 groupid를 가진 groupapplication하나씩 빼오기
            GroupApplication groupApplication = groupApplicationList.get(i);
            if(groupApplication != null){
                Long appliedUserIndex = groupApplication.getAppliedUser().getId();
                if (loginedUserIndex == appliedUserIndex) {

                    // 해당 group의 nowappliednum, hotpercent 수정
                    // 현재 참여 신청 인원 1 감소
                    int nowAppliedNum = groupApplication.getAppliedGroup().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum - 1;
                    groupApplication.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                    // 현재 참여 신청 가능한 인원 1 감소
                    int nowCanApplyNum = groupApplication.getAppliedGroup().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum + 1;
                    groupApplication.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = groupApplication.getAppliedGroup().getPeopleLimit();
                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                    groupApplication.getAppliedGroup().setHotPercent(updatedHotPercent);

                    // 참가 신청 이력 삭제
                    GroupApplication groupApplication2 = groupApplicationRepository.findByAppliedGroup_GroupIdAndAppliedUserId(groupId,loginedUserIndex);
                    groupApplicationRepository.deleteById(groupApplication2.getId());

                    group.getCanceledUser().add(loginedUser);

                }
            } else {
                throw new NullPointerException("참가 신청 이력이 존재하지 않습니다.");
            }
        }

    }


}
