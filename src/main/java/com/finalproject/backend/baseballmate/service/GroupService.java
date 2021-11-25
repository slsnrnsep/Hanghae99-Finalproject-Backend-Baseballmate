package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.groupChat.ChatRoomService;
import com.finalproject.backend.baseballmate.join.JoinRequests;
import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.HotGroupResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupApplicationRepository groupApplicationRepository;
    private final GroupLikesRepository groupLikesRepository;
    private final CanceledListRepository canceledListRepository;
    private final GroupCommentRepository groupCommentRepository;
    private String commonPath = "/images"; // 파일 저장할 기본 경로 변수 설정, 초기화
    private final AlarmService alarmService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    String[] picturelist = {"basic0.jpg","basic1.jpg","basic2.jpg","basic3.jpg","basic4.jpg","basic5.jpg","basic6.jpg","basic7.jpg","basic8.jpg","basic9.jpg"};
    Random random = new Random();


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
            boolean allowtype = group.isAllowtype();
            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath,selectTeam,dday,allowtype);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }
        return allGroupResponseDtoList;
    }

    // 내가 작성한 모임 조회(등록 순)
    public List<AllGroupResponseDto> getMywriteAllGroups(User userdetail) {
        List<Group> groupList = groupRepository.findAllByCreatedUser(userdetail);
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
            boolean allowtype = group.isAllowtype();
            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath,selectTeam,dday,allowtype);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }
        return allGroupResponseDtoList;
    }

    // 내가 참여한 모임 조회(등록 순)
    public List<AllGroupResponseDto> getMyapplicationAllGroups(User userdetail) {
        List<Group> groupList = new ArrayList<>();
        List<GroupApplication> mygroupApplicationList=groupApplicationRepository.findAllByAppliedUser(userdetail);
        for(int i=0; i<mygroupApplicationList.size();i++)
        {
            groupList.add(mygroupApplicationList.get(i).getAppliedGroup());
        }

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
            boolean allowtype = group.isAllowtype();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath,selectTeam,dday,allowtype);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }
        return allGroupResponseDtoList;
    }

    // 내가 좋아요한 모임 조회(등록 순)
    public List<AllGroupResponseDto> getMylikeAllGroups(User userdetail) {
        List<Group> groupList = new ArrayList<>();
        List<GroupLikes> mygrouplikeslist = groupLikesRepository.findAllByUserId(userdetail.getId());
        for(int i=0; i<mygrouplikeslist.size();i++)
        {
            groupList.add(mygrouplikeslist.get(i).getGrouplikes());
        }
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
            boolean allowtype = group.isAllowtype();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath,selectTeam,dday,allowtype);

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
            boolean allowtype = group.isAllowtype();
            AllGroupResponseDto allGroupResponseDto =
                    new AllGroupResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, stadium, groupDate, filePath, selectTeam,dday,allowtype);

            allGroupResponseDtoList.add(allGroupResponseDto);
        }

        return allGroupResponseDtoList;
    }
    // 핫한 모임 조회(hotPercent순) - 메인 페이지용
    public List<HotGroupResponseDto> getHotGroups2() {
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
            boolean allowtype = group.isAllowtype();
            HotGroupResponseDto hotGroupResponseDto =
                    new HotGroupResponseDto(groupId, createdUsername, title, peopleLimit, canApplyNum, hotPercent, stadium, groupDate,filePath,selectTeam,dday,allowtype);

            hotGroupResponseDtoList.add(hotGroupResponseDto);
        }
        return hotGroupResponseDtoList;
    }



    // 핫한 내가 응원하는 모임 조회(hotPercent순) - 메인 페이지용
    public List<HotGroupResponseDto> getHotGroups(String team) {
        List<Group> hotGroupList = groupRepository.searchTeamHotgroup(team);
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
            boolean allowtype = group.isAllowtype();
            HotGroupResponseDto hotGroupResponseDto =
                    new HotGroupResponseDto(groupId, createdUsername, title, peopleLimit, canApplyNum, hotPercent, stadium, groupDate,filePath,selectTeam,dday,allowtype);

            hotGroupResponseDtoList.add(hotGroupResponseDto);
        }
        return hotGroupResponseDtoList;
    }

    // 모임 생성
    @Transactional
    public Group createGroup(GroupRequestDto requestDto, User loginedUser) {
        Group Group = new Group(requestDto, loginedUser);
        groupRepository.save(Group);
        chatRoomService.createChatRoom(Group.getGroupId(), loginedUser);
        return Group;
    }

    // 모임 상세 조회
    public GroupDetailResponseDto getGroupDetail(Long id) {
        // 모임 entity에서 해당 모임에 대한 모든 정보 빼오기
        Group group = groupRepository.findByGroupId(id);
        List<Map<String, String>> appliedUsers = new ArrayList<>();

        if(group.getGroupApplications().size()!=0)
        {
            // 참여자 정보 리스트 만들기
            for (int i = 0; i < group.getGroupApplications().size(); i++) {
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
        }
        Long groupId = group.getGroupId();
        String createdUserName = group.getCreatedUsername();
        String createdUserId = group.getCreatedUser().getUserid();
        String createdUserProfileImg = group.getCreatedUser().getPicture();
        String title = group.getTitle();
        String content = group.getContent();
        int peopleLimit = group.getPeopleLimit();
        int nowAppliedNum = group.getNowAppliedNum();
        int canApplyNum = group.getCanApplyNum();
        double hotPercent = group.getHotPercent();
        String stadium = group.getStadium();
        String groupDate = group.getGroupDate();
        String filePath = group.getFilePath();
        List<GroupComment> groupcommentList = groupCommentRepository.findAllByGroup_GroupIdOrderByModifiedAtDesc(id);
        List<Map<String, String>> appliedUserInfo = appliedUsers;

        // D - day 계산
        int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
        int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
        LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
        Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
        String dday = countingday.toString();
        boolean allowtype = group.isAllowtype();
        GroupDetailResponseDto groupdetailResponseDto =
                new GroupDetailResponseDto(groupId, createdUserName, createdUserId, createdUserProfileImg, title, content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, stadium , groupDate, filePath, dday,allowtype, appliedUserInfo, groupcommentList);

        return groupdetailResponseDto;
    }


    // 모임 게시글 수정하기
    public void updateGroup(Long groupId, MultipartFile file, GroupRequestDto requestDto, UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 유저 로그인 체크
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Group group = groupRepository.findByGroupId(groupId);
        if(group != null) {
            createdUserId = group.getCreatedUser().getUserid();

            if(!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
            if (file != null) {
                String origFilename = file.getOriginalFilename();
                String filename = new MD5Generator(origFilename).toString() + "jpg";

                String savePath = System.getProperty("user.dir") + commonPath;

                // 파일이 저장되는 폴더가 없을 경우 폴더 생성
                if (!new java.io.File(savePath).exists()) {
                    try {
                        new java.io.File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

                // 이미지 파일 저장
                String filePath = savePath + "/" + filename;
                try{
                    file.transferTo(new File(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                group.setFilePath(filename);

            }
            if(file == null)
            {
                String filename = picturelist[random.nextInt(10)+1];
                group.setFilePath(filename);

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
    public void applyGroup(Long groupId, UserDetailsImpl userDetails) {
        // 참여 신청 들어온 groupid에 해당하는 그룹을 찾기
        Group appliedGroup = groupRepository.findByGroupId(groupId);


        Group group = new Group();
        User user = new User();
//        List<User> canceledUserList = appliedGroup.getCanceledUser();
//        List<Long> canceledUserInxList = new ArrayList<>();
//        for (int i=0; i< canceledUserList.size(); i++) {
//            Long canceledUserInx = canceledUserList.get(i).getId();
//            canceledUserInxList.add(canceledUserInx);
//        }
//        System.out.println("모임 참여 시 취소자 명단 리스트 : " + canceledUserList);
//        System.out.println("모임 참여 시 취소자 명단 리스트 : " + canceledUserInxList);
        if(!appliedGroup.isAllowtype())
        {
            throw new IllegalArgumentException("모임이 모집을 마감하였습니다.");
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        } else {
            User loginedUser = userDetails.getUser();
            String loginUser = userDetails.getUser().getUsername();

            GroupApplication groupApplication = groupApplicationRepository.findByAppliedGroupAndAppliedUser(appliedGroup, loginedUser);
            // 모임에 대한 해당 참가자의 참가 이력이 없고, 모임을 만든 사람이 아닌 유저가 참가 신청하는 경우 -> 이 경우만 참가 신청 가능
            if ((groupApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedGroup.getCreatedUser().getUserid()))) {

                // 모임이 참조하는 취소 리스트에서 해당 모임의 인덱스를 갖는 취소 리스트들 찾아오기
                List<CanceledList> canceledLists = canceledListRepository.findAllByCanceledGroup_GroupId(groupId);
//        List<Long> canceledUserInxList = new ArrayList<>();
                if (canceledLists.size() != 0) {
                    // 취소 리스트에서 참가 신청한 유저 찾기
                    for (int i=0; i< canceledLists.size(); i++) {
                        CanceledList canceledList = canceledLists.get(i);

                        // 참가 신청하는 모임의 취소 유저 리스트에 이름이 있지 않을 경우 -> 이 경우에만 참가 신청 가능
                        if (canceledList.getCanceledUser().getId().equals(loginedUser.getId()))
                        {
                            throw new IllegalArgumentException("취소후 재참가는 불가합니다.");
                        }
                        else {
                            GroupApplication application = new GroupApplication(loginedUser, appliedGroup);
                            groupApplicationRepository.save(application);

                            // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
                            // 현재 참여 신청한 인원 1 증가
                            int nowAppliedNum = application.getAppliedGroup().getNowAppliedNum();
                            int updatedAppliedNum = nowAppliedNum + 1;
                            application.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                            // 현재 참여 신청 가능한 인원 1 감소
                            int nowCanApplyNum = application.getAppliedGroup().getCanApplyNum();
                            int updatedCanApplyNum = nowCanApplyNum - 1;
                            application.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                            // 인기도 값 수정
                            int peopleLimit = application.getAppliedGroup().getPeopleLimit();
                            double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                            application.getAppliedGroup().setHotPercent(updatedHotPercent);

//                            String commentAlarm = loginedUser + "님 께서" + group.getTitle() + "모임에 지원하셨습니다";
//                            alarmService.alarmAppliedUser(commentAlarm,appliedGroup, user);

                        }
                    }
                } else {
                    // 취소 리스트가 아예 없을 경우 그냥 신청 가능
                    GroupApplication application1 = new GroupApplication(loginedUser, appliedGroup);
                    groupApplicationRepository.save(application1);

                    // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
                    // 현재 참여 신청한 인원 1 증가
                    int nowAppliedNum = application1.getAppliedGroup().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                    // 현재 참여 신청 가능한 인원 1 감소
                    int nowCanApplyNum = application1.getAppliedGroup().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = application1.getAppliedGroup().getPeopleLimit();
                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedGroup().setHotPercent(updatedHotPercent);


//                    String commentAlarm = loginedUser + "님 께서" + group.getTitle() + "모임에 지원하셨습니다";
//                    alarmService.alarmAppliedUser(commentAlarm,appliedGroup, user);
                }


            } else {
                throw new IllegalArgumentException("모임을 만들었거나 참가이력이 있습니다."); // 모임을 만든 사람이 요청하는 경우 or 참가 이력이 있는 경우
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
        // canceleduser에 넣을 리스트 생성
        List<User> addCanceledUserList = new ArrayList<>();

        // groupApplication에서 해당 groupid에 속하는 groupApplication들을 찾아오기
        List<GroupApplication> groupApplicationList = groupApplicationRepository.findAllByAppliedGroup(group);

        // 로그인 한 유저의 유저 객체와 인덱스 값 빼오기
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<groupApplicationList.size();j++)
        {
            testlist.add(groupApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUserIndex))
        {
            throw new IllegalArgumentException("참여신청 기록이 없습니다.");
        }


        for(int i=0; i<groupApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 groupid를 가진 groupapplication하나씩 빼오기
            GroupApplication groupApplication = groupApplicationList.get(i);

            // 참가 신청 취소를 요청하는 모임에 대한 신청 내역들이 있고
            if(groupApplication != null && groupApplication.getAppliedUser().getId().equals(loginedUserIndex)) {
                // 로그인 한 유저가 참가 신청을 했던 유저와 같다면

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


//                    groupApplicationRepository.findByAppliedGroupAndAppliedUser(group, loginedUser)
//                    groupApplicationRepository.deleteById(groupApplication2.getId());

                // 참가 신청 이력 삭제하기
                groupApplicationRepository.delete(groupApplication);

                // 취소 리스트에 추가하기
                CanceledList canceledList = new CanceledList(loginedUser, group);
                canceledListRepository.save(canceledList);

            }
        }


    }

    @Transactional
    public String denyGroup(Long groupId, UserDetailsImpl userDetails) {
        Group group = groupRepository.findByGroupId(groupId);
        if (group.getCreatedUser().getUserid().equals(userDetails.getUser().getUserid())) {
            if (group.isAllowtype()) {
                group.setAllowtype(false);
                // 확정되면 채팅방 형성되게
//                chatRoomService.createChatRoom(userDetails.getUser());
                return "모임 확정 완료. 이제부터 모집을 하지 못합니다.";
            } else {
                group.setAllowtype(true);
                return "모임 확정취소 완료. 이제부터 모집을 다시 할 수 있습니다.";
            }
        } else {
            throw new IllegalArgumentException("모임장만 확정이 가능합니다");
        }
    }

    // 모임 참여하기
    @Transactional
    public void applyGroup2(Long groupId,JoinRequests joinRequests) {
        // 참여 신청 들어온 groupid에 해당하는 그룹을 찾기
        Group appliedGroup = groupRepository.findByGroupId(groupId);

        if(!appliedGroup.isAllowtype())
        {
            throw new IllegalArgumentException("모임이 모집을 마감하였습니다.");
        }

            User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                    ()-> new IllegalArgumentException("조인에서 로그인유저를 찾을 수 없습니다.")
            );

            GroupApplication groupApplication = groupApplicationRepository.findByAppliedGroupAndAppliedUser(appliedGroup, loginedUser);
            // 모임에 대한 해당 참가자의 참가 이력이 없고, 모임을 만든 사람이 아닌 유저가 참가 신청하는 경우 -> 이 경우만 참가 신청 가능
            if ((groupApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedGroup.getCreatedUser().getUserid()))) {

                // 모임이 참조하는 취소 리스트에서 해당 모임의 인덱스를 갖는 취소 리스트들 찾아오기
                List<CanceledList> canceledLists = canceledListRepository.findAllByCanceledGroup_GroupId(groupId);
//        List<Long> canceledUserInxList = new ArrayList<>();
                if (canceledLists.size() != 0) {
                    // 취소 리스트에서 참가 신청한 유저 찾기
                    for (int i=0; i< canceledLists.size(); i++) {
                        CanceledList canceledList = canceledLists.get(i);

                        // 참가 신청하는 모임의 취소 유저 리스트에 이름이 있지 않을 경우 -> 이 경우에만 참가 신청 가능
                        if (canceledList.getCanceledUser().getId().equals(loginedUser.getId()))
                        {
                            throw new IllegalArgumentException("취소후 재참가는 불가합니다.");
                        }
                        else {
                            GroupApplication application = new GroupApplication(loginedUser, appliedGroup);
                            groupApplicationRepository.save(application);

                            // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
                            // 현재 참여 신청한 인원 1 증가
                            int nowAppliedNum = application.getAppliedGroup().getNowAppliedNum();
                            int updatedAppliedNum = nowAppliedNum + 1;
                            application.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                            // 현재 참여 신청 가능한 인원 1 감소
                            int nowCanApplyNum = application.getAppliedGroup().getCanApplyNum();
                            int updatedCanApplyNum = nowCanApplyNum - 1;
                            application.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                            // 인기도 값 수정
                            int peopleLimit = application.getAppliedGroup().getPeopleLimit();
                            double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                            application.getAppliedGroup().setHotPercent(updatedHotPercent);

//                            String commentAlarm = loginedUser + "님 께서" + group.getTitle() + "모임에 지원하셨습니다";
//                            alarmService.alarmAppliedUser(commentAlarm,appliedGroup, user);

                        }
                    }
                } else {
                    // 취소 리스트가 아예 없을 경우 그냥 신청 가능
                    GroupApplication application1 = new GroupApplication(loginedUser, appliedGroup);
                    groupApplicationRepository.save(application1);

                    // 참여 신청과 동시에 해당 group의 nowappliednum, hotpercent 수정
                    // 현재 참여 신청한 인원 1 증가
                    int nowAppliedNum = application1.getAppliedGroup().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedGroup().setNowAppliedNum(updatedAppliedNum);

                    // 현재 참여 신청 가능한 인원 1 감소
                    int nowCanApplyNum = application1.getAppliedGroup().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedGroup().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = application1.getAppliedGroup().getPeopleLimit();
                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedGroup().setHotPercent(updatedHotPercent);


//                    String commentAlarm = loginedUser + "님 께서" + group.getTitle() + "모임에 지원하셨습니다";
//                    alarmService.alarmAppliedUser(commentAlarm,appliedGroup, user);
                }


            } else {
                throw new IllegalArgumentException("모임을 만들었거나 참가이력이 있습니다."); // 모임을 만든 사람이 요청하는 경우 or 참가 이력이 있는 경우
            }
        }


    // 모임 취소하기
    // 1. 그룹에서 해당 그룹의 groupapplication list를 불러오기
    // 2. 거기에서 유저 아이디를 찾고, 로그인 한 유저 아이디와 일치하는지 확인하기
    // 3. 일치하면 해당 groupapplication을 삭제하기
    // 4. 그룹 내의 취소 리스트에 id값 추가하기

    @Transactional
    public void cancelApplication2(Long groupId, JoinRequests joinRequests) {
        Group group = groupRepository.findByGroupId(groupId);
        // canceleduser에 넣을 리스트 생성
        List<User> addCanceledUserList = new ArrayList<>();

        // groupApplication에서 해당 groupid에 속하는 groupApplication들을 찾아오기
        List<GroupApplication> groupApplicationList = groupApplicationRepository.findAllByAppliedGroup(group);

        // 로그인 한 유저의 유저 객체와 인덱스 값 빼오기
        User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("조인에서 로그인유저를 찾을 수 없습니다.")
        );


        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<groupApplicationList.size();j++)
        {
            testlist.add(groupApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUser.getId()))
        {
            throw new IllegalArgumentException("참여신청 기록이 없습니다.");
        }


        for(int i=0; i<groupApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 groupid를 가진 groupapplication하나씩 빼오기
            GroupApplication groupApplication = groupApplicationList.get(i);

            // 참가 신청 취소를 요청하는 모임에 대한 신청 내역들이 있고
            if(groupApplication != null && groupApplication.getAppliedUser().getId().equals(loginedUser.getId())) {
                // 로그인 한 유저가 참가 신청을 했던 유저와 같다면

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


//                    groupApplicationRepository.findByAppliedGroupAndAppliedUser(group, loginedUser)
//                    groupApplicationRepository.deleteById(groupApplication2.getId());

                // 참가 신청 이력 삭제하기
                groupApplicationRepository.delete(groupApplication);

                // 취소 리스트에 추가하기
                CanceledList canceledList = new CanceledList(loginedUser, group);
                canceledListRepository.save(canceledList);

            }
        }


    }
}


