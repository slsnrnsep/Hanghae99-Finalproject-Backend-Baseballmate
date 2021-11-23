package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GoodsLikesController {

    private final GoodsLikesService goodsLikesService;

    @PostMapping("/goods/{goodsId}/like")
    public String GoodsLikePost(
            @PathVariable("goodsId") Long goodsId,
            @RequestBody GoodsLikesReqeustDto goodsLikesReqeustDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인한 사용자만 가능한 기능입니다");
        }
        User loginUser = userDetails.getUser();
        boolean goodsLiked = goodsLikesService.goodsLiked(goodsId, goodsLikesReqeustDto, userDetails, loginUser);

        if(goodsLiked)
        {
            return "true";
        }
        else
        {
            return "false";
        }

    }



}
