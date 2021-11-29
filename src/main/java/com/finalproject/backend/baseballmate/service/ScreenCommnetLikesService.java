package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.ScreenComment;
import com.finalproject.backend.baseballmate.model.ScreenCommentLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenCommentLikesRepository;
import com.finalproject.backend.baseballmate.repository.ScreenCommentRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ScreenCommnetLikesService {

    private final ScreenCommentLikesRepository screenCommentLikesRepository;
    private final UserRepository userRepository;
    private final ScreenCommentRepository screenCommentRepository;
    private final AlarmService alarmService;

    @Transactional
    public boolean screenCommentLikes(Long screencommentId, LikesRequestDto likesRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );
        ScreenComment screenComment = screenCommentRepository.findById(screencommentId).orElseThrow(
                () -> new IllegalArgumentException("모임이 존재하지 않습니다.")
        );

        if(likesRequestDto.getIsLiked().equals("true")){
            ScreenCommentLikes likes = screenCommentLikesRepository.findByScreenComment_ScreenCommentIdAndUserId(screenComment.getScreenCommentId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );
        user.deleteScreenCommentLikes(likes);
        screenComment.deleteLikes(likes);
        screenCommentLikesRepository.delete(likes);
        return false;
        }else{
            if(screenCommentLikesRepository.findByScreenComment_ScreenCommentIdAndUserId(screenComment.getScreenCommentId(), user.getId()).isPresent())
            {
                return true;
            }
            ScreenCommentLikes likes = screenCommentLikesRepository.save(new ScreenCommentLikes(screenComment,user));
            user.addScreenCommentLikes(likes);
            screenComment.addLikes(likes);
            User alarmuser = userRepository.findById(screenComment.getCommentUserIndex()).orElseThrow(
                    () -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다")
            );
            alarmService.alarmMethod(alarmuser,user.getUsername(),screenComment.getComment(),"댓글","좋아요를 표시했습니다!",screenComment.getScreen().getScreenId(),"screen");
            return true;
        }
    }
}
