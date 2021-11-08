package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsCommentLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GoodsCommentLikesController {

    private final GoodsCommentLikesService goodsCommentLikesService;

    @PostMapping("/goods/{goodsId}/comment/{commentId}/like")
    public String GoodsCommentLikePost(
            @PathVariable("commentId") Long goodscommentId,
            @RequestBody GoodsLikesReqeustDto reqeustDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만이 가능한 기능입니다");
        }
        boolean goodsCommentLikes = goodsCommentLikesService.goodsCommentLikes(goodscommentId, reqeustDto, userDetails);

        if(goodsCommentLikes)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
