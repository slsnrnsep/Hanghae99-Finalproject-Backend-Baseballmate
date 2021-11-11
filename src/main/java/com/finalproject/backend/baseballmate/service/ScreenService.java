package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.CanceledScreenListRepository;
import com.finalproject.backend.baseballmate.repository.ScreenApplicationRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.requestDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ScreenDetailResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ScreenApplicationRepository screenApplicationRepository;
    private final CanceledScreenListRepository canceledScreenListRepository;

    @Transactional
    public Screen createScreen(ScreenRequestDto requestDto, User loginedUser) {
        Screen screen = new Screen(requestDto,loginedUser);
        screenRepository.save(screen);
        return screen;
    }


    public List<AllScreenResponseDto> getAllScreens() {
        List<Screen> screenList = screenRepository.findAllByOrderByCreatedAtDesc();
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        for(int i=0; i<screenList.size(); i++){
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
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();

            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum , hotPercent, groupDate, filePath, selectPlace,placeInfomation,dday);
            allScreenResponseDtos.add(allScreenResponseDto);
        }
        return allScreenResponseDtos;
    }

    public List<AllScreenResponseDto> showScreenByregion(String location, Pageable pageable) {
//        PageRequest
        Page<Screen> grouppage = screenRepository.findByPlaceInfomation(location,pageable);
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        List<Screen> groupList=grouppage.getContent();
        for(int i=0; i<groupList.size(); i++) {
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
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();

            AllScreenResponseDto allGroupResponseDto =
                    new AllScreenResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace,placeInfomation,dday);

            allScreenResponseDtos.add(allGroupResponseDto);
        }

        return allScreenResponseDtos;
    }


    // 스크린 야구 상세조회
    public ScreenDetailResponseDto getScreenDetails(Long id) {
        Screen screen = screenRepository.findByScreenId(id);

        Long screenId = screen.getScreenId();
        String title = screen.getTitle();
        String createdUsername = screen.getCreatedUsername();
        String content = screen.getContent();
        int peopleLimit = screen.getPeopleLimit();
        int nowAppliedNum = screen.getNowAppliedNum();
        int canApplyNum = screen.getCanApplyNum();
        double hotPercent = screen.getHotPercent();
        String groupDate = screen.getGroupDate();
        String filePath = screen.getFilePath();
        List<ScreenComment> screenCommentList = screen.getScreenCommentList();

        ScreenDetailResponseDto screenDetailResponseDto =
                new ScreenDetailResponseDto(screenId, title, createdUsername, content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, groupDate, filePath,screenCommentList);
        return screenDetailResponseDto;
    }
    // 스크린 야구 모임 수정
    @Transactional
    public void updateScreen(Long screenId, ScreenRequestDto requestDto, UserDetailsImpl userDetails){
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if(screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if(!loginedUserId.equals(createdUserId)){
                throw new IllegalArgumentException("수정권한이 없습니다");
            }
            screen.updateScreen(requestDto);
            screenRepository.save(screen);
        }else {
            throw new IllegalArgumentException("해당 게시물이 존재하지 않습니다");
        }

    }
    @Transactional
    public void deleteScreen(Long screenId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if(screen != null){
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if(!loginedUserId.equals(createdUserId)){
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
            screenRepository.deleteById(screenId);
        }else {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다");
        }
    }
    // 스크린 야구 모임 참여하기
    @Transactional
    public void applyScreen(Long screenId, UserDetailsImpl userDetails) {
//        List<User> cancleUserList = appliedScreen.getCanceledUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);

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
                    if(canceledScreenList.getCanceledUser().getId() == loginedUser.getId()){
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
                }
            } else {
            throw new IllegalArgumentException("참가 신청이 불가합니다."); // 모임을 만든 사람이 요청하는 경우 or 참가 이력이 있는 경우
            }
        }
    }




    @Transactional
    public void cancleApplication(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screenApplicationRepository.findAllByAppliedScreen(screen);
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 screenId를 가진 groupapplication하나씩 빼오기
            ScreenApplication screenApplication = screenApplicationList.get(i);
            // 참가 신청 취소를 요청하는 모임에 대한 신청 내역들이 있고
            if (screenApplication != null) {
                Long appliedUserIndex = screenApplication.getAppliedUser().getId();
                // 로그인 한 유저가 참가 신청을 했던 유저와 같다면
                if (loginedUserIndex == appliedUserIndex) {
                    // 현재 참여 신청 인원 1 감소
                    int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum - 1;
                    screenApplication.getAppliedScreen().setNowAppliedNum(updatedAppliedNum);

                    // 현재 참여 신청 가능한 인원 1 감소
                    int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum + 1;
                    screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // 인기도 값 수정
//                    int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
//                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
//                    screenApplication.getAppliedScreen().setHotPercent(updatedHotPercent);

                    // 참가 신청 이력 삭제하기
                    screenApplicationRepository.delete(screenApplication);

                    // 취소 리스트에 추가하기
                    CanceledScreenList canceledScreenList = new CanceledScreenList(loginedUser, screen);
                    canceledScreenListRepository.save(canceledScreenList);
                    } else {
                        throw new NullPointerException("참가 신청 이력이 존재하지 않습니다."); // '참가 신청을 했던 유저가 아님'을 의미
                    }
                } else {
                    throw new NullPointerException("참가 신청 이력이 존재하지 않습니다."); // 'group에 참가 신청을 한 사람이 없음'을 의미
            }
        }
    }
}
