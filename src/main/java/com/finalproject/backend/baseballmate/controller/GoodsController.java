package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"4. 굿즈"}) // Swagger
public class GoodsController {

    private final GoodsService goodsService;

    // 굿즈 생성
    @ApiOperation(value = "(Legacy)굿즈 게시글 작성", notes = "(Legacy)굿즈 게시글을 작성합니다.")
    @PostMapping( "/goods/legacy")
    public GoodsResponseDto postGoodsLegacy(@RequestPart(value = "file",required = false) MultipartFile files, GoodsRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return goodsService.createGoodsLegacy(userDetails, requestDto,files);
    }

    @ApiOperation(value = "굿즈 게시글 작성", notes = "굿즈 게시글을 작성합니다.")
    @PostMapping( "/goods")
    public GoodsResponseDto postGoods(@RequestBody GoodsRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return goodsService.createGoods(userDetails, requestDto);
    }

    // 굿즈 전체 조회
    @ApiOperation(value = "굿즈 게시글 전체 조회", notes = "굿즈 게시글을 전체 조회합니다.")
    @GetMapping("/goods")
    public List<AllGoodsResponseDto> getGoods() throws ParseException {
        return goodsService.getGoods();
    }

    // 굿즈 상세 조회
    @ApiOperation(value = "굿즈 게시글 상세 조회", notes = "굿즈 게시글중 1개를 상세 조회합니다.")
    @GetMapping("/goods/{goodsId}")
    public GoodsDetailResponseDto getGoodsDetail(@PathVariable("goodsId") Long goodsId) {
        return goodsService.getGoodsDetail(goodsId);
    }

    // 긋즈 수정
    @ApiOperation(value = "굿즈 게시글 수정", notes = "굿즈 게시글을 수정합니다.")
    @PutMapping("/goods/{goodsId}")
    public GoodsResponseDto updateGoods(@PathVariable("goodsId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody GoodsRequestDto requestDto) {
        return goodsService.updateGoods(id, requestDto,userDetails);
    }

    // 굿즈 삭제
    @ApiOperation(value = "굿즈 게시글 삭제", notes = "굿즈 게시글을 삭제합니다.")
    @DeleteMapping("/goods/{goodsId}")
    public GoodsResponseDto deleteGoods(@PathVariable("goodsId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsService.deleteGoods(id,userDetails);
    }
}
