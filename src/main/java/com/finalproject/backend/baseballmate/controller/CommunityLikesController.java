package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.CommunityLikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommunityLikesController {

    private final CommunityLikesService communityLikesService;

    @PostMapping("/community/{communityId}/like")
    public String CommunityLikePost(
            @PathVariable("communityId") Long communityId,
            @RequestBody CommunityLikesRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만 가능한 기능입니다");
        }
        boolean commnunityLiked = communityLikesService.communityLiked(communityId, requestDto, userDetails);

        if(commnunityLiked)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
