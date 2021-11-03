package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.PhoneRequstDto;
import com.finalproject.backend.baseballmate.service.PhoneService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
public class PhoneController {

    private PhoneService phoneService;

    public PhoneController(PhoneService phoneService){
        this.phoneService = phoneService;
    }


    @PostMapping("/checkPhone")
    public void sendMessage(@RequestBody PhoneRequstDto requstDto){
        Random rand = new Random();
        String ranNum = "";
        for(int i = 0; i<4; i++){
            String ran = Integer.toString(rand.nextInt(10));
            ranNum+= ran;
        }

        phoneService.sendMessage(requstDto,ranNum);
        System.out.println("하이이");
        System.out.println(ranNum);

    }
}
