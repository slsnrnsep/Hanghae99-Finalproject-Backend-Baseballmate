package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.FileService;
import com.finalproject.backend.baseballmate.service.GoodsService;
import com.finalproject.backend.baseballmate.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GoodsController {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final GoodsService goodsService;
    private String commonPath = "/images";
    private final FileService fileService;

    // 굿즈생성
    @PostMapping("/goods")
    public GoodsResponseDto postGoods(

            @RequestParam(value = "file",required = false) MultipartFile files,

            GoodsRequestDto requestDto)
//            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
//        if(userDetails == null)
//        {
//            throw new IllegalArgumentException("로그인 한 사용자만 등록 가능합니다");
//        }

        try
        {

            String filename = "basic.jpg";
            if (files != null) {
                String origFilename = files.getOriginalFilename();
                filename = new MD5Generator(origFilename).toString() + ".jpg";
                /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */

                String savePath = System.getProperty("user.dir") + commonPath;
                /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                //files.part.getcontententtype() 해서 이미지가 아니면 false처리해야함.
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "\\" + filename;// 이경로는 우분투랑 윈도우랑 다르니까 주의해야댐 우분투 : / 윈도우 \\ 인것같음.
                files.transferTo(new File(filePath));
            }
            requestDto.setGoodsImg(filename);

//            User loginUser = userDetails.getUser();
//            String loginedUsername = userDetails.getUser().getUsername();
            User loginUser = userRepository.findByUsername("aaa").orElseThrow(
                    ()-> new IllegalArgumentException("유저찾을수 없슴")
            );
            goodsService.createGoods(loginUser, requestDto);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","등록완료");
            return goodsResponseDto;
        }

        catch (Exception e)
        {
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("","에러발생");
            return goodsResponseDto;
        }
    }

    // 굿즈 전체조회
    @GetMapping("/goods")
    public List<AllGoodsResponseDto> getGoods() throws ParseException
    {
        List<AllGoodsResponseDto> allGoods = goodsService.getGoods();
        return allGoods;
    }

    // 굿즈페이지 상세조회
    @GetMapping("/goods/{goodsId}")
    public GoodsDetailResponseDto getGoodsDetail(@PathVariable("goodsId") Long goodsId)
    {
        GoodsDetailResponseDto goodsDetailResponseDto = goodsService.getGoodsDetail(goodsId);
        return goodsDetailResponseDto;
    }

    // 긋즈 수정
    @PutMapping("/goods/{goodsId}")
    public GoodsResponseDto updateGoods(
            @PathVariable("goodsId") Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody GoodsRequestDto requestDto)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 사용자만이 수정할 수 있습니다");
        }

        try
        {
            goodsService.updateGoods(id, requestDto,userDetails);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","변경완료");
            return goodsResponseDto;
        }

        catch (Exception e)
        {
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("","에러발생");
            return goodsResponseDto;
        }

    }

    // 굿즈 삭제
    @DeleteMapping("/goods/{goodsId}")
    public GoodsResponseDto deleteGoods(
            @PathVariable("goodsId") Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 사용자만이 삭제할 수 있습니다");
        }

        goodsService.deleteGoods(id,userDetails);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","삭제완료");
        return goodsResponseDto;


    }
}
