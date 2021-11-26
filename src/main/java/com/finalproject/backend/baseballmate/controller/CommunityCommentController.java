package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.CommunityCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping("/community/{communityId}/comment")
    public MsgResponseDto createComment(
            @RequestBody CommunityCommentRequestDto commentRequestDto,
            @PathVariable("communityId") Long communityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        User loginedUser = userDetails.getUser();
        communityCommentService.createComment(commentRequestDto, communityId, loginedUser);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");
        return msgResponseDto;
    }
    // 커뮤 내 댓글 수정
    @PutMapping("/community/{communityId}/comment/{commentId}")
    public MsgResponseDto updateComment(
            @PathVariable("communityId") Long communityId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommunityCommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        communityCommentService.updateCommunityComment(communityId, commentId, commentRequestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }
    // 커뮤내 댓글 삭제
    @DeleteMapping("/community/{community}/comment/{commentId}")
    public MsgResponseDto deleteCommunityComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

        communityCommentService.deleteCommunityComment(commentId,userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
        return msgResponseDto;
    }

}
