package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupCommentLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupCommentLikesController {

    private final GroupCommentLikesService groupCommentLikesService;

    @PostMapping("/groups/{groupId}/comment/{commentId}/like")
    public String GroupCommentLikePost(
            @PathVariable("groupId") Long groupId,
            @PathVariable("commentId") Long groupcommentId,
            @RequestBody LikesRequestDto likesRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만 가능한 기능입니다");
        }

        boolean groupCommentLikes = groupCommentLikesService.groupCommentLikes(groupcommentId, likesRequestDto, userDetails);

        if(groupCommentLikes)
        {
            return "true";
        }
        else
        {
            return "false";
        }

    }
}
