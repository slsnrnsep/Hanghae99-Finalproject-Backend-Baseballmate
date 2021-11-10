package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenCommnetLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScreenCommentLikesController {

    private final ScreenCommnetLikesService screenCommnetLikesService;

    @PostMapping("/screen/{screenId}/comment/{commentId}/like")
    public String ScreenCommentLikePost(
            @PathVariable("screenId") Long screenId,
            @PathVariable("commentId") Long screencommentId,
            @RequestBody LikesRequestDto likesRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만 가능한 기능입니다");
        }
        boolean screenCommentLikes = screenCommnetLikesService.screenCommentLikes(screencommentId, likesRequestDto, userDetails);

        if(screenCommentLikes)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }

}
