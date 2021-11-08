package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupCommentLikesService;
import com.finalproject.backend.baseballmate.service.GroupLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupLikesController {

    private final GroupLikesService groupLikesService;

    @PostMapping("/groups/{groupId}/like")
    public String GroupLikePost(
            @PathVariable("groupId") Long groupId,
            @RequestBody LikesRequestDto likesRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만 가능한 기능입니다");
        }

        boolean groupLikes = groupLikesService.groupLikes(groupId, likesRequestDto, userDetails);

        if(groupLikes)
        {
            return "true";
        }
        else
        {
            return "false";
        }

    }
}
