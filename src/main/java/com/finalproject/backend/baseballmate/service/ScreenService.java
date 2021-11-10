package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenApplication;
import com.finalproject.backend.baseballmate.model.ScreenComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenApplicationRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.requestDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.responseDto.ScreenDetailResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ScreenApplicationRepository screenApplicationRepository;

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

            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum , hotPercent, groupDate, filePath, selectPlace);
            allScreenResponseDtos.add(allScreenResponseDto);
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
    public void applyScreen(User appliedUser, Screen appliedScreen) {
        List<User> cancleUserList = appliedScreen.getCanceledUser();

        // 참가 이력 조회
        ScreenApplication screenApplication1 = screenApplicationRepository.findByAppliedScreenAndAndAppliedUser(appliedScreen, appliedUser);
        if(screenApplication1 != null){
            throw new IllegalArgumentException("참가 신청 이력이 존재합니다");
        }else {
            if(screenApplication1 == null && cancleUserList.contains(appliedUser)){
                throw new IllegalArgumentException("재참가는 불가능합니다");
            }else{
                ScreenApplication screenApplication = new ScreenApplication(appliedUser,appliedScreen);
                screenApplicationRepository.save(screenApplication);

                int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                int updateAppliedNum = nowAppliedNum + 1;
                screenApplication.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                int updatedCanApplyNum = nowCanApplyNum - 1;
                screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

//                int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
//                double updateHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
//                screenApplication.getAppliedScreen().setHotPercent(updateHotPercent);
            }
        }


    }
    @Transactional
    public void cancleApplication(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screen.getScreenApplications();
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // 참가 신청 취소를 요청한 screenId를 가진 groupapplication하나씩 빼오기
            ScreenApplication screenApplication = screenApplicationList.get(i);
            if (screenApplication != null) {
                Long appliedUserIndex = screenApplication.getAppliedUser().getId();
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

                    ScreenApplication screenApplication2 = screenApplicationRepository.findByAppliedScreenScreenIdAndAppliedUserId(screenId, loginedUserIndex);
                    screenApplicationRepository.deleteById(screenApplication2.getId());

                    screen.getCanceledUser().add(loginedUser);
                    }
                }
                else {
                    throw new NullPointerException("참가 신청 이력이 존재하지 않습니다.");
                }
        }
    }
}
