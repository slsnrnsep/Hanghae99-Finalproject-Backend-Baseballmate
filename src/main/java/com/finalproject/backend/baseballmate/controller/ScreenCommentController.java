package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ScreenCommentController {

    private final ScreenCommentService screenCommentService;
    // 모임내 댓글 생성
    @PostMapping("/screen/{screenId}/comment")
    public MsgResponseDto createScreenComment(
            @PathVariable("screenId") Long screenId,
            @RequestBody ScreenCommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인후 사용가능합니다");
        }
        User loginedUser = userDetails.getUser();
        screenCommentService.createComment(commentRequestDto, screenId, loginedUser);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");

        return msgResponseDto;

    }

    // 모임 내 댓글 수정
    @PutMapping("/screen/{screenId}/comment/{commentId}")
    public MsgResponseDto updateScreenComment(
            @PathVariable("screenId") Long screenId,
            @PathVariable("commentId") Long commentid,
            @RequestBody ScreenCommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 후 사용할 수 있습니다");
        }
        screenCommentService.updateScreenComment(screenId, commentid, requestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }
    // 모임 내 댓글 삭제
    @DeleteMapping("/screen/{screenId}/comment/{commentId}")
    public MsgResponseDto deleteScreenComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        screenCommentService.deleteScreenComment(commentId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
        return msgResponseDto;
    }
}
