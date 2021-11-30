package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenCommentRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ScreenCommentService {

    private final ScreenCommentRepository screenCommentRepository;
    private final ScreenRepository screenRepository;
    private final AlarmService alarmService;

    @Transactional
    public MsgResponseDto createComment(ScreenCommentRequestDto commentRequestDto, Long screenId, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인후 사용가능합니다");
        }
        User loginedUser = userDetails.getUser();
        String loginedUsername = loginedUser.getUsername();
        String loginedUserId = loginedUser.getUserid();
        String loginedUserPicture = loginedUser.getPicture();
        Long loginedUserIndex = loginedUser.getId();
        Screen screen = screenRepository.findByScreenId(screenId);
        ScreenComment screenComment = new ScreenComment(commentRequestDto, screen, loginedUsername, loginedUserIndex, loginedUserId, loginedUserPicture);
        screenCommentRepository.save(screenComment);
        screen.getScreenCommentList().add(screenComment);

        alarmService.alarmMethod(screen.getScreenCreatedUser(),loginedUsername,screen.getTitle(),"스야모임","댓글을 남기셨습니다.",screenId,"screen");
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");

        return msgResponseDto;
    }
    @Transactional
    public MsgResponseDto updateScreenComment(Long screenId, Long commentid, ScreenCommentRequestDto requestDto, UserDetailsImpl userDetails) {
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 후 사용할 수 있습니다");
        }

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
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
            return msgResponseDto;
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
    @Transactional
    public MsgResponseDto deleteScreenComment(Long commentId, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

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
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
            return msgResponseDto;
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
}
