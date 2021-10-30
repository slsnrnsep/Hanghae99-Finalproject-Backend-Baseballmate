package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.GoodsCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsCommentResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GoodsCommentController {
    private final GoodsCommentService goodsCommentService;

    @PostMapping("/page/goods/detail/{goodsId}/comment")
    public GoodsCommentResponseDto postComment(@RequestBody GoodsCommentRequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable("goodsId") Long goodsid ){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 사용자만 가능한 기능입니다");
        }
        try {
            User loginUser = userDetails.getUser();
            goodsCommentService.createComment(loginUser, requestDto, goodsid);
            GoodsCommentResponseDto goodsCommentResponseDto = new GoodsCommentResponseDto("success", "등록완료");
            return goodsCommentResponseDto;
        } catch (Exception e) {
            GoodsCommentResponseDto goodsCommentResponseDto = new GoodsCommentResponseDto("","에러발생");
            return goodsCommentResponseDto;
        }
    }

    @PutMapping("/page/goods/detail/{goodsCommentId}")
    public GoodsCommentResponseDto updateComment(@PathVariable("goodsCommentId") Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody GoodsCommentRequestDto requestDto){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 사용자만이 수정할 수 있습니다");
        }
        try {
            goodsCommentService.updateGoodsComment(userDetails, id, requestDto);
            GoodsCommentResponseDto goodsCommentResponseDto = new GoodsCommentResponseDto("success","변경완료");
            return goodsCommentResponseDto;
        } catch (Exception e) {
            GoodsCommentResponseDto goodsCommentResponseDto = new GoodsCommentResponseDto("","에러발생");
            return goodsCommentResponseDto;
        }
    }
    @DeleteMapping("/page/goods/detail/{goodsCommentId}")
    public GoodsCommentResponseDto deleteComment(@PathVariable("goodsCommentId") Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 사용자만이 삭제할 수 있습니다");
        }
        goodsCommentService.deleteComment(id, userDetails);
        GoodsCommentResponseDto goodsCommentResponseDto = new GoodsCommentResponseDto("success","삭제완료");
        return goodsCommentResponseDto;
    }

}
