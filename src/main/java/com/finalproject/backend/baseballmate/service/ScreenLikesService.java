package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenLikesRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ScreenLikesService {

    private final UserRepository userRepository;
    private final ScreenRepository screenRepository;
    private final ScreenLikesRepository screenLikesRepository;

    @Transactional
    public boolean screenLikes(Long screenId, LikesRequestDto likesRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다")
        );
        Screen screen = screenRepository.findById(screenId).orElseThrow(
                () -> new IllegalArgumentException("스크인야구 모임이 존재하지 않습니다")
        );
        if(likesRequestDto.getIsLiked().equals("true")){
            ScreenLikes likes = screenLikesRepository.findByScreenLikeIdAndUserId(screen.getScreenId(),user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 모임에 좋아요 이력이 없습니다")
            );
            user.deleteScreenLikes(likes);
            screen.deleteLikes(likes);
            screenLikesRepository.delete(likes);
            return false;
        }else{
            if(screenLikesRepository.findByScreenLikeIdAndUserId(screen.getScreenId(), user.getId()).isPresent())
            {
                return true;
            }
            ScreenLikes likes = screenLikesRepository.save(new ScreenLikes(screen, user));
            user.addScreenLikes(likes);
            screen.addLikes(likes);
            return true;
        }
    }
}
