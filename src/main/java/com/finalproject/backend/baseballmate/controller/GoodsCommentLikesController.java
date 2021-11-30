package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsCommentLikesService;
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
@Api(tags = {"4. 굿즈"}) // Swagger
public class GoodsCommentLikesController {

    private final GoodsCommentLikesService goodsCommentLikesService;

    //굿즈 댓글 좋아요 기능
    @ApiOperation(value = "굿즈 댓글 좋아요/취소", notes = "굿즈 댓글글을 좋아요하거나 취소합니다.")
    @PostMapping("/goods/{goodsId}/comment/{commentId}/like")
    public String GoodsCommentLikePost(@PathVariable("goodsId") Long goodsId,@PathVariable("commentId") Long commentId, @RequestBody GoodsLikesReqeustDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsCommentLikesService.goodsCommentLikes(commentId, requestDto, userDetails);
    }
}
