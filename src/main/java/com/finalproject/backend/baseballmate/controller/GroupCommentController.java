package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupCommentRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupCommentController {

    private final GroupCommentService commentService;
    private final GroupCommentRepository groupCommentRepository;

    // 모임 게시글 내 댓글 수정하기
    @PutMapping("/groups/{groupId}/comment/{commentId}")
    public MsgResponseDto updateGroupComment(@PathVariable("groupId") Long groupid,@PathVariable("commentId") Long commentid,@RequestBody GroupCommentRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        commentService.updateGroupComment(groupid, commentid, requestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }
    // 모임 게시글 내 댓글 삭제하기
    @DeleteMapping("/groups/{groupId}/comment/{commentId}")
    public MsgResponseDto delteGroupComment(@PathVariable("commentId") Long id,@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        commentService.deleteGroupComment(id,userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
        return msgResponseDto;
    }

    // 모임 게시글 내 댓글 생성하기
    @PostMapping("/groups/{groupId}/comment")
    public MsgResponseDto createGroupComment(@RequestBody GroupCommentRequestDto commentRequestDto, @PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        User loginedUser = userDetails.getUser();
        commentService.createComment(commentRequestDto, groupId, loginedUser);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");
        return msgResponseDto;
    }


}
