package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsRepository goodsRepository;
    private final GoodsService goodsService;

    // 굿즈생성
    @PostMapping("/goods")
    public GoodsResponseDto postGoods(@RequestBody GoodsRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 한 사용자만 등록 가능합니다");
        }
        try {
            User loginUser = userDetails.getUser();
            String loginedUsername = userDetails.getUser().getUsername();
            goodsService.createGoods(loginUser, requestDto);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","등록완료");
            return goodsResponseDto;
        } catch (Exception e) {
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("","에러발생");
            return goodsResponseDto;
        }
    }

    // 굿즈 전체조회
    @GetMapping("/goods")
    public List<AllGoodsResponseDto> getGoods() throws ParseException {
        List<AllGoodsResponseDto> allGoods = goodsService.getGoods();
        return allGoods;
    }

    // 굿즈페이지 상세조회
    @GetMapping("/page/goods/detail/{goodsId}")
    public GoodsDetailResponseDto getGoodsDetail(@PathVariable("goodsId") Long goodsId){
        GoodsDetailResponseDto goodsDetailResponseDto = goodsService.getGoodsDetail(goodsId);
        return goodsDetailResponseDto;
    }

    // 긋즈 수정
    @PutMapping("/page/goods/{goodsId}")
    public GoodsResponseDto updateGoods(@PathVariable("goodsId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody GoodsRequestDto requestDto){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 사용자만이 수정할 수 있습니다");
        }
        try {
            goodsService.updateGoods(id, requestDto,userDetails);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","변경완료");
            return goodsResponseDto;
        } catch (Exception e) {
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("","에러발생");
            return goodsResponseDto;
        }
    }
    // 굿즈 삭제
    @DeleteMapping("/page/goods/{goodsId}")
    public GoodsResponseDto deleteGoods(@PathVariable("goodsId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 사용자만이 삭제할 수 있습니다");
        }
            goodsService.deleteGoods(id,userDetails);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","삭제완료");
            return goodsResponseDto;


    }
}
