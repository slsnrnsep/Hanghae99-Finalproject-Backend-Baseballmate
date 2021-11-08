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
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
            HotGroupResponseDto hotGroupResponseDto =
                    new HotGroupResponseDto(groupId, createdUsername, title, peopleLimit, canApplyNum, hotPercent, stadium, groupDate,filePath,selectTeam);

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

        GroupDetailResponseDto groupdetailResponseDto =
                new GroupDetailResponseDto(groupId, createdUserName, title, content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, stadium , groupDate,groupcommentList,filePath);

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
        GroupApplication groupApplication = new GroupApplication(appliedUser, appliedGroup);
        groupApplicationRepository.save(groupApplication);

        // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
        // 현재 참여 신청한 인원 1 증가
        int nowAppliedNum = groupApplication.getAppliedGroup().getNowAppliedNum();
        int updatedAppliedNum = nowAppliedNum + 1;
        groupApplication.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

        // 현재 참여 신청 가능한 인원 1 감소
        int nowCanApplyNum = groupApplication.getAppliedGroup().getCanApplyNum();
        int updatedCanApplyNum = nowCanApplyNum -1;
        groupApplication.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

        // 인기도 값 수정
        int peopleLimit = groupApplication.getAppliedGroup().getPeopleLimit();
        double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
        groupApplication.getAppliedGroup().setHotPercent(updatedHotPercent);
    }


}

// 모임 전체 조회(등록 순)
//    public AllGroupResponseDto getAllGroups() {
//        List<Group> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
//        AllGroupResponseDto allGroupResponseDto = new AllGroupResponseDto("success", groupList);
//        System.out.println(allGroupResponseDto);
//        return allGroupResponseDto;
//    }

// 모임 상세 조회
//    public GroupDetailResponseDto getGroupDetail(Long groupId) {
//        // 모임 entity에서 해당 모임에 대한 모든 정보 빼오기
//        Group group = groupRepository.findByGroupId(groupId);
//
//
//        String createdUserName = group.getCreatedUsername();
//        String title = group.getTitle();
//        String content = group.getContent();
//        int peopleLimit = group.getPeopleLimit();
//        int nowAppliedNum = getNowAppliedNum(groupId);
//        int canApplyNum = (peopleLimit - nowAppliedNum);
//        double hotPercent = getUpdatedHotPercent(groupId);
//        String stadium = group.getStadium();
//        String groupDate = group.getGroupDate();
//        List<GroupComment> groupcommentList = group.getGroupCommentList();
//
//        GroupDetailResponseDto groupdetailResponseDto =
//                new GroupDetailResponseDto(createdUserName, title, content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, stadium , groupDate, groupcommentList);
//
//        return groupdetailResponseDto;
//    }

// 어떤 모임의 핫한 정도 업데이트하기
// 나중에 주기적으로 알아서 update 하도록 구현하기(몇 시간마다 업데이트 한다던가)
//    public double getUpdatedHotPercent(Long groupId) {
//        // (현재 신청 인원수/모임 최대 인원수) * 100 퍼센트 구하기
//        Group group = groupRepository.findByGroupId(groupId);
//        int peopleLimit = group.getPeopleLimit();
//        int nowAppliedNum = getNowAppliedNum(groupId);
////        int canApplyNum = (peopleLimit - nowAppliedNum);
//        // 현재 hotPercent 구하기
//        double hotPercent = ((double) nowAppliedNum / (double) peopleLimit * 100.0);
//        group.updateHotPercent(hotPercent);
//        Group updatedGroup = groupRepository.save(group);
//        return updatedGroup.getHotPercent();
//    }

// 어떤 모임의 핫한 정도 수정하기
//    public double updateHotPercent(Long groupId) {
//        Group group = groupRepository.findByGroupId(groupId);
//        double groupHotPercent = getHotPercent(groupId);
//        double updatedHotPercent = group.setHotPercent(groupHotPercent);
//        return updatedHotPercent;
//    }

// 모임 ID별로 현재 참여신청 인원 구하기
//    public int getNowAppliedNum(Long groupId) {
//        List<GroupApplication> groupApplications = groupApplicationRepository.findAll();
//
//        List<Long> appliedGroupIdList = new ArrayList<>();
//        for (int i=0; i<groupApplications.size(); i++) {
//            GroupApplication groupApplication = groupApplications.get(i);
//            Long appliedGroupId = groupApplication.getAppliedGroup().getGroupId();
//            appliedGroupIdList.add(appliedGroupId);
//        }
//
//        int nowAppliedNum = 0;
//        for (int l=0; l<appliedGroupIdList.size(); l++) {
//            Long appliedGroupId = appliedGroupIdList.get(l);
//            if (groupId == appliedGroupId) {
//                nowAppliedNum = nowAppliedNum + 1;
//            }
//        }
//        return nowAppliedNum;
//    }


// hotPercent 구하기
//    public double newHotPercent() {
//        Group group = new Group();
//        int peopleLimit = group.getPeopleLimit();
//        int nowAppliedNum = group.getNowAppliedNum();
//        double nowHotPercent = ((double) nowAppliedNum / (double) peopleLimit * 100.0);
//        group.setHotPercent(nowHotPercent);
//    }