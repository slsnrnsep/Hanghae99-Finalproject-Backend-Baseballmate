package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenCommentRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ScreenCommentService {

    private final ScreenCommentRepository screenCommentRepository;
    private final ScreenRepository screenRepository;

    @Transactional
    public void createComment(ScreenCommentRequestDto commentRequestDto, Long screenId, User loginedUser) {
        String loginedUsername = loginedUser.getUsername();
        String loginedUserId = loginedUser.getUserid();
        String loginedUserPicture = loginedUser.getPicture();
        Long loginedUserIndex = loginedUser.getId();
        Screen screen = screenRepository.findByScreenId(screenId);
        ScreenComment screenComment = new ScreenComment(commentRequestDto, screen, loginedUsername, loginedUserIndex, loginedUserId, loginedUserPicture);
        screenCommentRepository.save(screenComment);
        screen.getScreenCommentList().add(screenComment);

    }
    @Transactional
    public void updateScreenComment(Long screenId, Long commentid, ScreenCommentRequestDto requestDto, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String commentUserId = "";

        ScreenComment screenComment = screenCommentRepository.findByScreenCommentId(commentid);
        if(screenComment != null)
        {
            commentUserId = screenComment.getCommentUserId();

            if(!loginedUserId.equals(commentUserId)){
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
            screenComment.updateScreenComment(requestDto);
            screenCommentRepository.save(screenComment);
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
    @Transactional
    public void deleteScreenComment(Long commentId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String commentUserId = "";

        ScreenComment screenComment = screenCommentRepository.findByScreenCommentId(commentId);
        if(screenComment != null)
        {
            commentUserId = screenComment.getCommentUserId();

            if(!loginedUserId.equals(commentUserId)){
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            screenCommentRepository.deleteById(commentId);
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
}
