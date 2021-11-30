package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.CommunityCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(tags = {"7. 커뮤니티"}) // Swagger
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @ApiOperation(value = "커뮤니티 댓글 생성", notes = "토큰과 커뮤니티아이디로 게시글을 생성합니다.")
    @PostMapping("/community/{communityId}/comment")
    public MsgResponseDto createComment(@RequestBody CommunityCommentRequestDto commentRequestDto, @PathVariable("communityId") Long communityId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityCommentService.createComment(commentRequestDto, communityId, userDetails);
    }

    // 커뮤 내 댓글 수정
    @ApiOperation(value = "커뮤니티 댓글 수정", notes = "토큰과 커뮤니티아이디로 게시글을 수정합니다.")
    @PutMapping("/community/{communityId}/comment/{commentId}")
    public MsgResponseDto updateComment(@PathVariable("communityId") Long communityId, @PathVariable("commentId") Long commentId, @RequestBody CommunityCommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityCommentService.updateCommunityComment(communityId, commentId, commentRequestDto, userDetails);
    }

    // 커뮤내 댓글 삭제
    @ApiOperation(value = "커뮤니티 댓글 삭제", notes = "토큰과 커뮤니티아이디로 게시글을 삭제합니다.")
    @DeleteMapping("/community/{communityId}/comment/{commentId}")
    public MsgResponseDto deleteCommunityComment(@PathVariable("communityId") Long communityId,@PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityCommentService.deleteCommunityComment(commentId,userDetails);
    }

}
