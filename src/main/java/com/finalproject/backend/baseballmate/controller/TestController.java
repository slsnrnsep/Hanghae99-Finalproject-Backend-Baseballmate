//package com.finalproject.backend.baseballmate.controller;
//
//import com.finalproject.backend.baseballmate.config.DbSetConfig;
//import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@RestController
//public class TestController {
//    private final DbSetConfig dbSetConfig;
//
//    @GetMapping("/dbSet1")
//    public MsgResponseDto testDB1() throws IOException {
//        dbSetConfig.dbset1();
//        MsgResponseDto msgResponseDto = new MsgResponseDto("OK","유저DB셋업 성공");
//        return msgResponseDto;
//    }
//
//    @GetMapping("/dbSet2")
//    public MsgResponseDto testDB2()
//    {
////        dbSetConfig.dbset2();
//        MsgResponseDto msgResponseDto = new MsgResponseDto("OK","모임,타임라인,굿즈DB 셋업 성공");
//        return msgResponseDto;
//    }
//
//    @GetMapping("/dbSet3")
//    public MsgResponseDto testDB3()
//    {
////        dbSetConfig.dbset3();
//        MsgResponseDto msgResponseDto = new MsgResponseDto("OK","댓글DB셋업 성공");
//        return msgResponseDto;
//    }
//
//    @GetMapping("/dbSet4")
//    public MsgResponseDto testDB4() throws IOException {
////        dbSetConfig.dbset1();
////        dbSetConfig.dbset2();
////        dbSetConfig.dbset3();
//        MsgResponseDto msgResponseDto = new MsgResponseDto("OK","모든 테스트DB셋업성공");
//        return msgResponseDto;
//    }
//}