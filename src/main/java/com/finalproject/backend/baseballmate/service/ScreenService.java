package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenComment;
import com.finalproject.backend.baseballmate.model.User;
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
}
