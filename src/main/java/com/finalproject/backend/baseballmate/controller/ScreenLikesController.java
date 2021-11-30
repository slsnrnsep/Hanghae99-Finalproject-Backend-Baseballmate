package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenLikesService;
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
@Api(tags = {"3. 스크린모임"}) // Swagger
public class ScreenLikesController {

    private final ScreenLikesService screenLikesService;

    //스크린모임 좋아요 기능
    @ApiOperation(value = "스크린모임 좋아요/취소", notes = "스크린모임을 좋아요하거나 취소합니다.")
    @PostMapping("/screen/{screenId}/like")
    public String ScreenLikePost(@PathVariable("screenId") Long screenId, @RequestBody LikesRequestDto likesRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenLikesService.screenLikes(screenId,likesRequestDto,userDetails);
    }
}
