package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsLikesService;
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
public class GoodsLikesController {

    private final GoodsLikesService goodsLikesService;

    //굿즈 좋아요 기능
    @ApiOperation(value = "굿즈 좋아요/취소", notes = "굿즈를 좋아요하거나 취소합니다.")
    @PostMapping("/goods/{goodsId}/like")
    public String GoodsLikePost(@PathVariable("goodsId") Long goodsId, @RequestBody GoodsLikesReqeustDto goodsLikesReqeustDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsLikesService.goodsLiked(goodsId, goodsLikesReqeustDto, userDetails);
    }
}
