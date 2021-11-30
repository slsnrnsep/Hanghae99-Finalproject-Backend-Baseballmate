package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.CommunityLikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityLikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Api(tags = {"7. 커뮤니티"}) // Swagger
public class CommunityLikesController {

    private final CommunityLikesService communityLikesService;

    //커뮤니티 좋아요 기능
    @ApiOperation(value = "커뮤니티 좋아요/취소", notes = "커뮤니티 게시글을 좋아요하거나 취소합니다.")
    @PostMapping("/community/{communityId}/like")
    public String CommunityLikePost(@PathVariable("communityId") Long communityId, @RequestBody CommunityLikesRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityLikesService.communityLiked(communityId, requestDto, userDetails);
    }
}
