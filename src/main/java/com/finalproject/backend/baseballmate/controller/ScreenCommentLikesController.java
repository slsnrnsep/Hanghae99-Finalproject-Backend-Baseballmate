package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenCommnetLikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"3. 스크린모임"}) // Swagger
public class ScreenCommentLikesController {

    private final ScreenCommnetLikesService screenCommnetLikesService;

    @ApiOperation(value = "스크린모임 댓글 좋아요/취소", notes = "스크린모임 댓글글을 좋아요하거나 취소합니다.")
    @PostMapping("/screen/{screenId}/comment/{commentId}/like")
    public String ScreenCommentLikePost(@PathVariable("screenId") Long screenId, @PathVariable("commentId") Long commentId, @RequestBody LikesRequestDto likesRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenCommnetLikesService.screenCommentLikes(commentId, likesRequestDto, userDetails);
    }
}
