package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ScreenLikesController {

    private final ScreenLikesService screenLikesService;

    @PostMapping("/screen/{screenId}/like")
    public String ScreenLikePost(
            @PathVariable("screenId") Long screenId,
            @RequestBody LikesRequestDto likesRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만이 이용가능합니다");
        }
        boolean screenLikes = screenLikesService.screenLikes(screenId,likesRequestDto,userDetails);

        if(screenLikes)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
