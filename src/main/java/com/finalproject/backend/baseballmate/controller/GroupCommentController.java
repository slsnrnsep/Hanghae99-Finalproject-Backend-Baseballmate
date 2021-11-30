package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@Api(tags = {"2. 모임"}) // Swagger
public class GroupCommentController {

    private final GroupCommentService commentService;

    // 모임 게시글 내 댓글 생성하기
    @ApiOperation(value = "모임 댓글 작성", notes = "모임의 댓글을 작성합니다.")
    @PostMapping("/groups/{groupId}/comment")
    public MsgResponseDto createGroupComment(@RequestBody GroupCommentRequestDto commentRequestDto, @PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, groupId, userDetails);
    }

    // 모임 게시글 내 댓글 수정하기
    @ApiOperation(value = "모임 댓글 수정", notes = "모임의 댓글을 수정합니다.")
    @PutMapping("/groups/{groupId}/comment/{commentId}")
    public MsgResponseDto updateGroupComment(@PathVariable("groupId") Long groupId, @PathVariable("commentId") Long commentId, @RequestBody GroupCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateGroupComment(groupId, commentId, requestDto, userDetails);
    }

    // 모임 게시글 내 댓글 삭제하기
    @ApiOperation(value = "모임 댓글 삭제", notes = "모임의 댓글을 삭제합니다.")
    @DeleteMapping("/groups/{groupId}/comment/{commentId}")
    public MsgResponseDto deleteGroupComment(@PathVariable("groupId") Long groupId,@PathVariable("commentId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteGroupComment(id,userDetails);
    }

}
