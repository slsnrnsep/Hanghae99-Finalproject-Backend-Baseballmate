package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.PhoneRequstDto;
import com.finalproject.backend.baseballmate.responseDto.PhoneResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.PhoneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"9-2. 휴대폰인증"}) // Swagger
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(@Lazy PhoneService phoneService){
        this.phoneService = phoneService;
    }

    @PostMapping("/checkPhone")
    @ApiOperation(value = "휴대폰 메시지 전송", notes = "휴대폰 메시지 전송")
    public void sendMessage(@RequestBody PhoneRequstDto requstDto){
        try
        {
            phoneService.sendMessage(requstDto);
        } catch (Exception e)
        {
            throw new IllegalArgumentException("번호를 잘못입력했거나,서버에 오류가 발생하였습니다");
        }
    }

    @PostMapping("/confirmNumChk")
    @ApiOperation(value = "인증번호 일치 확인", notes = "인증번호 일치 확인")
    public PhoneResponseDto confirmNumChk(@RequestBody PhoneRequstDto requstDto){
        phoneService.confirmNumChk(requstDto);
        return new PhoneResponseDto("success","인증완료");
    }

    @PostMapping("/custommsg")
    @ApiOperation(value = "커스텀 메시지 전송", notes = "커스텀 메시지 전송")
    public void sendcustommsg(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "테스트메시지입니다";
        phoneService.CustomsendMessage(msg,userDetails.getUser());
    }
}
