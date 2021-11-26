package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.groupChat.ChatRoomService;
import com.finalproject.backend.baseballmate.join.JoinRequests;
import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.responseDto.HotScreenResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ScreenDetailResponseDto;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ScreenApplicationRepository screenApplicationRepository;
    private final ScreenCommentRepository screenCommentRepository;
    private final ScreenLikesRepository screenLikesRepository;
    private String commonPath = "/images";
    private final UserRepository userRepository;
    private final CanceledScreenListRepository canceledScreenListRepository;
    private final ChatRoomService chatRoomService;


    @Transactional
    public Screen createScreen(ScreenRequestDto requestDto, User loginedUser) {
        Screen screen = new Screen(requestDto, loginedUser);
        screenRepository.save(screen);
        chatRoomService.createChatRoom2(screen.getScreenId(), loginedUser);
        return screen;
    }


    public List<AllScreenResponseDto> getAllScreens() {
        List<Screen> screenList = screenRepository.findAllByOrderByCreatedAtDesc();
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtos.add(allScreenResponseDto);
        }
        return allScreenResponseDtos;
    }

    public List<AllScreenResponseDto> showScreenByregion(String location, Pageable pageable) {
//        PageRequest
        Page<Screen> grouppage = screenRepository.findByPlaceInfomation(location, pageable);
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        List<Screen> groupList = grouppage.getContent();
        for (int i = 0; i < groupList.size(); i++) {
            Screen group = groupList.get(i);

            Long groupId = group.getScreenId();
            String title = group.getTitle();
            String createdUsername = group.getCreatedUsername();
            int peopleLimit = group.getPeopleLimit();
            int canApplyNum = group.getCanApplyNum();
            double hotPercent = group.getHotPercent();
            String groupDate = group.getGroupDate();
            String filePath = group.getFilePath();
            String selectPlace = group.getSelectPlace();
            String placeInfomation = group.getPlaceInfomation();

            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = group.isAllowtype();
            AllScreenResponseDto allGroupResponseDto =
                    new AllScreenResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);

            allScreenResponseDtos.add(allGroupResponseDto);
        }

        return allScreenResponseDtos;
    }


    // 스크린 야구 상세조회
    public ScreenDetailResponseDto getScreenDetails(Long id) {
        Screen screen = screenRepository.findByScreenId(id);
        List<Map<String, String>> appliedUsers = new ArrayList<>();

        if (screen.getScreenApplications().size() != 0) {
            // 참여자 정보 리스트 만들기
            for (int i = 0; i < screen.getScreenApplications().size(); i++) {
                ScreenApplication screenApplication = screen.getScreenApplications().get(i);
                String appliedUserInx = screenApplication.getAppliedUser().getId().toString();
                String appliedUserId = screenApplication.getAppliedUser().getUserid();
                String appliedUsername = screenApplication.getAppliedUser().getUsername();
                String appliedUserImage = screenApplication.getAppliedUser().getPicture();

                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("UserImage", appliedUserImage);
                userInfo.put("Username", appliedUsername);
                userInfo.put("UserId", appliedUserId);
                userInfo.put("UserInx", appliedUserInx);

                appliedUsers.add(i, userInfo);
            }
        }
        Long screenId = screen.getScreenId();
        String createdUserId = screen.getScreenCreatedUser().getUserid();
        String createdUserProfileImg = screen.getScreenCreatedUser().getPicture();
        String title = screen.getTitle();
        String createdUsername = screen.getCreatedUsername();
        String content = screen.getContent();
        int peopleLimit = screen.getPeopleLimit();
        int nowAppliedNum = screen.getNowAppliedNum();
        int canApplyNum = screen.getCanApplyNum();
        double hotPercent = screen.getHotPercent();
        String groupDate = screen.getGroupDate();
        String filePath = screen.getFilePath();
        String placeInfomation = screen.getPlaceInfomation();
        List<ScreenComment> screenCommentList = screenCommentRepository.findAllByScreenScreenIdOrderByModifiedAtDesc(id);
        List<Map<String, String>> appliedUserInfo = appliedUsers;

        // D - day 계산
        int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
        int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
        LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
        Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
        String dday = countingday.toString();
        boolean allowtype = screen.isAllowtype();
        ScreenDetailResponseDto screenDetailResponseDto =
                new ScreenDetailResponseDto(screenId, createdUsername, createdUserId, createdUserProfileImg, title,content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, groupDate, filePath,dday,placeInfomation,allowtype,appliedUserInfo,screenCommentList);

        return screenDetailResponseDto;
    }

    // 스크린 야구 모임 수정
    @Transactional
    public void deleteScreen(Long screenId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
            screenRepository.deleteById(screenId);
        } else {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다");
        }
    }

    // 스크린 야구 모임 참여하기
    @Transactional
    public void applyScreen(Long screenId, UserDetailsImpl userDetails) {
//        List<User> cancleUserList = appliedScreen.getCanceledUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);

        if(!appliedScreen.isAllowtype())
        {
            throw new IllegalArgumentException("모임이 모집을 마감하였습니다.");
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        } else {
            User loginedUser = userDetails.getUser();

        ScreenApplication screenApplication = screenApplicationRepository.findByAppliedScreenAndAndAppliedUser(appliedScreen, loginedUser);
        // 모임에 대한 해당 참가자의 참가 이력이 없고, 모임을 만든 사람이 아닌 유저가 참가 신청하는 경우 -> 이 경우만 참가 신청 가능
        if((screenApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedScreen.getScreenCreatedUser().getUserid()))){

            // 모임이 참조하는 취소 리스트에서 해당 모임의 인덱스를 갖는 취소 리스트들 찾아오기
            List<CanceledScreenList> canceledScreenLists = canceledScreenListRepository.findAllByCancledScreen_ScreenId(screenId);
            if(canceledScreenLists.size() != 0) {
                for(int i=0; i<canceledScreenLists.size(); i++){
                    CanceledScreenList canceledScreenList = canceledScreenLists.get(i);
                    if(canceledScreenList.getCanceledUser().getId().equals(loginedUser.getId())){
                        throw new IllegalArgumentException("모임 취소후 재참가는 불가합니다");
                        }
                    else {
                        ScreenApplication application = new ScreenApplication(loginedUser,appliedScreen);
                        screenApplicationRepository.save(application);

                        int nowAppliedNum = application.getAppliedScreen().getNowAppliedNum();
                        int updateAppliedNum = nowAppliedNum + 1;
                        application.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                        int nowCanApplyNum = application.getAppliedScreen().getCanApplyNum();
                        int updatedCanApplyNum = nowCanApplyNum - 1;
                        application.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                        // 인기도 값 수정
                        int peopleLimit = application.getAppliedScreen().getPeopleLimit();
                        double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                        application.getAppliedScreen().setHotPercent(updatedHotPercent);

                        }
                    }
                } else {
                    // 취소 리스트가 아예 없을 경우 그냥 신청 가능
                    ScreenApplication application1 = new ScreenApplication(loginedUser, appliedScreen);
                    screenApplicationRepository.save(application1);

                    int nowAppliedNum = application1.getAppliedScreen().getNowAppliedNum();
                    int updateAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                    int nowCanApplyNum = application1.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = application1.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedScreen().setHotPercent(updatedHotPercent);
                }
            } else {
                throw new IllegalArgumentException("모임을 만들었거나 참가이력이 있습니다."); // 모임을 만든 사람이 요청하는 경우 or 참가 이력이 있는 경우
            }
        }
    }


    @Transactional
    public void cancleApplication(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screenApplicationRepository.findAllByAppliedScreen(screen);
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<screenApplicationList.size();j++)
        {
            testlist.add(screenApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUserIndex))
        {
            throw new IllegalArgumentException("참여신청 기록이 없습니다.");
        }

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 screenId를 가진 groupapplication하나씩 빼오기
            ScreenApplication screenApplication = screenApplicationList.get(i);
            // 참가 신청 취소를 요청하는 모임에 대한 신청 내역들이 있고
            if (screenApplication != null && screenApplication.getAppliedUser().getId().equals(loginedUserIndex)) {

                // 로그인 한 유저가 참가 신청을 했던 유저와 같다면
                    // 현재 참여 신청 인원 1 감소
                    int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum - 1;
                    screenApplication.getAppliedScreen().setNowAppliedNum(updatedAppliedNum);

                    // 현재 참여 신청 가능한 인원 1 감소
                    int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum + 1;
                    screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                    screenApplication.getAppliedScreen().setHotPercent(updatedHotPercent);

                    // 참가 신청 이력 삭제하기
                    screenApplicationRepository.delete(screenApplication);

                    // 취소 리스트에 추가하기
                    CanceledScreenList canceledScreenList = new CanceledScreenList(loginedUser, screen);
                    canceledScreenListRepository.save(canceledScreenList);

                }
        }
    }


    public void updateScreen(Long screenId, MultipartFile file, ScreenRequestDto requestDto, UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 유저 로그인 체크
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
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
                try {
                    file.transferTo(new File(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                screen.setFilePath(filename);

            }
            screen.updateScreen(requestDto);
            screenRepository.save(screen);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    public List<AllScreenResponseDto> getMywriteAllScreens(User userdetail) {
        List<Screen> screenList = screenRepository.findAllByScreenCreatedUser(userdetail);
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }

    // 내가 참여한 모임 조회
    public List<AllScreenResponseDto> getMyapplicationAllScreens(User userdetail) {
        List<Screen> screenList = new ArrayList<>();
        List<ScreenApplication> myscreenApplicationList = screenApplicationRepository.findAllByAppliedUser(userdetail);
        for (int i = 0; i < myscreenApplicationList.size(); i++) {
            screenList.add(myscreenApplicationList.get(i).getAppliedScreen());
        }
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }

    public List<AllScreenResponseDto> getMylikeAllScreens(User userdetail) {
        List<Screen> screenList = new ArrayList<>();
        List<ScreenLikes> myscreenlikeslist = screenLikesRepository.findAllByUserId(userdetail.getId());
        for(int i = 0; i<myscreenlikeslist.size(); i++)
        {
            screenList.add(myscreenlikeslist.get(i).getScreenlikes());
        }
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }


    @Transactional
    public String denyScreen(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen.getScreenCreatedUser().getUserid().equals(userDetails.getUser().getUserid())) {
            if (screen.isAllowtype()) {
                screen.setAllowtype(false);
                return "모임 확정 완료. 이제부터 모집을 하지 못합니다.";
            } else {
                screen.setAllowtype(true);
                return "모임 확정취소 완료. 이제부터 모집을 다시 할 수 있습니다.";
            }
        } else {
            throw new IllegalArgumentException("모임장만 확정이 가능합니다");
        }
    }
    // 스야 최신순 조회
    @Transactional
    public List<AllScreenResponseDto> getnowScreen(int number) throws ParseException
    {
        List<Screen> screenList = screenRepository.findAllByOrderByCreatedAtDesc();
        List<AllScreenResponseDto> data = new ArrayList<>();

        if(screenList.size()<=number){
            number = screenList.size();
        }
        for(int i = 0; i<screenList.size(); i++){
            Screen screen = screenList.get(i);

            Long id = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(id, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday, allowtype);
            data.add(allScreenResponseDto);
        }
        return data;
    }
    // 스야 인기순 조회
    public List<HotScreenResponseDto> getHotScreen() {
        List<Screen> hotScreenList = screenRepository.findAllByOrderByHotPercentDesc();
        List<HotScreenResponseDto> hotScreenResponseDtoList = new ArrayList<>();

        for(int i = 0; i<hotScreenList.size(); i++){
            Screen screen = hotScreenList.get(i);

            Long id = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();

            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();

            HotScreenResponseDto hotScreenResponseDto =
                    new HotScreenResponseDto(id, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday, allowtype);

            hotScreenResponseDtoList.add(hotScreenResponseDto);
        }
        return hotScreenResponseDtoList;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 스크린 야구 모임 참여하기
    @Transactional
    public void applyScreen2(Long screenId, JoinRequests joinRequests) {
//        List<User> cancleUserList = appliedScreen.getCanceledUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);

        if(!appliedScreen.isAllowtype())
        {
            throw new IllegalArgumentException("모임이 모집을 마감하였습니다.");
        }
        User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("조인에서 로그인유저를 찾을 수 없습니다.")
        );

            ScreenApplication screenApplication = screenApplicationRepository.findByAppliedScreenAndAndAppliedUser(appliedScreen, loginedUser);
            // 모임에 대한 해당 참가자의 참가 이력이 없고, 모임을 만든 사람이 아닌 유저가 참가 신청하는 경우 -> 이 경우만 참가 신청 가능
            if((screenApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedScreen.getScreenCreatedUser().getUserid()))){

                // 모임이 참조하는 취소 리스트에서 해당 모임의 인덱스를 갖는 취소 리스트들 찾아오기
                List<CanceledScreenList> canceledScreenLists = canceledScreenListRepository.findAllByCancledScreen_ScreenId(screenId);
                if(canceledScreenLists.size() != 0) {
                    for(int i=0; i<canceledScreenLists.size(); i++){
                        CanceledScreenList canceledScreenList = canceledScreenLists.get(i);
                        if(canceledScreenList.getCanceledUser().getId().equals(loginedUser.getId())){
                            throw new IllegalArgumentException("모임 취소후 재참가는 불가합니다");
                        }
                        else {
                            ScreenApplication application = new ScreenApplication(loginedUser,appliedScreen);
                            screenApplicationRepository.save(application);

                            int nowAppliedNum = application.getAppliedScreen().getNowAppliedNum();
                            int updateAppliedNum = nowAppliedNum + 1;
                            application.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                            int nowCanApplyNum = application.getAppliedScreen().getCanApplyNum();
                            int updatedCanApplyNum = nowCanApplyNum - 1;
                            application.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                            // 인기도 값 수정
                            int peopleLimit = application.getAppliedScreen().getPeopleLimit();
                            double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                            application.getAppliedScreen().setHotPercent(updatedHotPercent);

                        }
                    }
                } else {
                    // 취소 리스트가 아예 없을 경우 그냥 신청 가능
                    ScreenApplication application1 = new ScreenApplication(loginedUser, appliedScreen);
                    screenApplicationRepository.save(application1);

                    int nowAppliedNum = application1.getAppliedScreen().getNowAppliedNum();
                    int updateAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                    int nowCanApplyNum = application1.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
                    int peopleLimit = application1.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedScreen().setHotPercent(updatedHotPercent);
                }
            } else {
                throw new IllegalArgumentException("모임을 만들었거나 참가이력이 있습니다."); // 모임을 만든 사람이 요청하는 경우 or 참가 이력이 있는 경우
            }
        }



    @Transactional
    public void cancleApplication2(Long screenId, JoinRequests joinRequests) {
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screenApplicationRepository.findAllByAppliedScreen(screen);
        User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("조인에서 로그인유저를 찾을 수 없습니다.")
        );

        Long loginedUserIndex = loginedUser.getId();

        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<screenApplicationList.size();j++)
        {
            testlist.add(screenApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUserIndex))
        {
            throw new IllegalArgumentException("참여신청 기록이 없습니다.");
        }

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 screenId를 가진 groupapplication하나씩 빼오기
            ScreenApplication screenApplication = screenApplicationList.get(i);
            // 참가 신청 취소를 요청하는 모임에 대한 신청 내역들이 있고
            if (screenApplication != null && screenApplication.getAppliedUser().getId().equals(loginedUserIndex)) {

                // 로그인 한 유저가 참가 신청을 했던 유저와 같다면
                // 현재 참여 신청 인원 1 감소
                int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                int updatedAppliedNum = nowAppliedNum - 1;
                screenApplication.getAppliedScreen().setNowAppliedNum(updatedAppliedNum);

                // 현재 참여 신청 가능한 인원 1 감소
                int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                int updatedCanApplyNum = nowCanApplyNum + 1;
                screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                // 인기도 값 수정
                int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
                double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                screenApplication.getAppliedScreen().setHotPercent(updatedHotPercent);

                // 참가 신청 이력 삭제하기
                screenApplicationRepository.delete(screenApplication);

                // 취소 리스트에 추가하기
                CanceledScreenList canceledScreenList = new CanceledScreenList(loginedUser, screen);
                canceledScreenListRepository.save(canceledScreenList);

            }
        }
    }
}