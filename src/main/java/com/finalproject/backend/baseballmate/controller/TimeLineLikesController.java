package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.TimeLineLikesService;
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
@Api(tags = {"1. 타임라인"}) // Swagger
public class TimeLineLikesController {

    private final TimeLineLikesService timeLineLikesService;

    //타임라인 좋아요 기능
    @ApiOperation(value = "타임라인 좋아요/취소", notes = "타임라인을 좋아요하거나 취소합니다.")
    @PostMapping("/timelines/{timeLineId}/like")
    public String likeTimeline(@PathVariable("timeLineId") Long timeLineId, @RequestBody LikesRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return timeLineLikesService.liked(timeLineId, requestDto, userDetails);
    }
}