package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupCommentRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GroupCommentController {
    private final GroupCommentService commentService;

    // 모임 게시글 내 댓글 생성하기
    @PostMapping("/page/group/detail/{groupId}/comment")
    public MsgResponseDto createGroupComment(@RequestBody GroupCommentRequestDto commentRequestDto, @PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginedUser = userDetails.getUser();
        commentService.createComment(commentRequestDto, groupId, loginedUser);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");
        return msgResponseDto;
    }
}
