package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.TimeLineLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TimeLineLikesController {

    private final TimeLineLikesService timeLineLikesService;

    @PostMapping("/timelines/{timeLineId}/like")
    public String likePost(@PathVariable("timeLineId") Long timeLineId, @RequestBody LikesRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //id postid
        //isliked 좋아요상태
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }

        boolean liked = timeLineLikesService.liked(timeLineId, requestDto, userDetails);
        if (liked) {
            return "true";
        } else {
            return "false";
        }

    }

}