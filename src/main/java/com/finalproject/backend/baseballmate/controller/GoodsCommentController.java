package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsCommentResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"4. 굿즈"}) // Swagger
public class GoodsCommentController {

    private final GoodsCommentService goodsCommentService;

    //굿즈 댓글 작성
    @ApiOperation(value = "굿즈 댓글 작성", notes = "굿즈의 댓글을 작성합니다.")
    @PostMapping("/goods/{goodsId}/comment")
    public GoodsCommentResponseDto postComment(@RequestBody GoodsCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("goodsId") Long goodsId ) {
        return goodsCommentService.createComment(userDetails, requestDto, goodsId);
    }

    //굿즈 댓글 수정
    @ApiOperation(value = "굿즈 댓글 수정", notes = "굿즈의 댓글을 수정합니다.")
    @PutMapping("/goods/{goodsId}/comment/{commentId}")
    public GoodsCommentResponseDto updateComment(@PathVariable("goodsId") Long goodsId, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody GoodsCommentRequestDto requestDto) {
        return goodsCommentService.updateGoodsComment(userDetails, commentId, requestDto);
    }

    //굿즈 댓글 삭제
    @ApiOperation(value = "굿즈 댓글 삭제", notes = "굿즈의 댓글을 삭제합니다.")
    @DeleteMapping("/goods/{goodsId}/comment/{commentId}")
    public GoodsCommentResponseDto deleteComment(@PathVariable("goodsId") Long goodsId, @PathVariable("commentId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsCommentService.deleteComment(id, userDetails);
    }
}
