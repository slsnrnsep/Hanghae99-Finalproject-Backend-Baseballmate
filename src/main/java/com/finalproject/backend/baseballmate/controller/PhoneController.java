package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.PhoneRequstDto;
import com.finalproject.backend.baseballmate.responseDto.PhoneResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.PhoneService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
//@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(@Lazy PhoneService phoneService){
        this.phoneService = phoneService;
    }

    @PostMapping("/checkPhone")
    public void sendMessage(@RequestBody PhoneRequstDto requstDto){
        //중복된 핸드폰번호 가입막기 추가할 예정

        try
        {
            phoneService.sendMessage(requstDto);

        } catch (Exception e)
        {
            throw new IllegalArgumentException("번호를 잘못입력했거나,서버에 오류가 발생하였습니다");
        }

    }

    @PostMapping("/confirmNumChk")
    public PhoneResponseDto confirmNumChk(@RequestBody PhoneRequstDto requstDto){

        phoneService.confirmNumChk(requstDto);

        PhoneResponseDto responseDto = new PhoneResponseDto("success","인증완료");

        return responseDto;
    }
}
