package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.service.AllChatInfoService;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"5. 채팅방"}) // Swagger

public class AllChatInfoController {

    private final AllChatInfoService allChatInfoService;

    //채팅방 유저목록 받아오기
    @ApiOperation(value = "채팅방의 유저목록 받아오기", notes = "채팅방아이디로 채팅방안의 유저목록을 조회합니다.")
    @GetMapping("/chat/user/{roomId}")
    public List<User> getUser(@PathVariable Long roomId){
        return allChatInfoService.getUser(roomId);
    }

    // 채팅방 나가기
    @ApiOperation(value = "참여했던 채팅방 나가기", notes = "토큰과 채팅방아이디로 채팅방을 탈퇴합니다.")
    @DeleteMapping("/chat/user/{roomId}")
    public void deleteAllChatInfo(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        allChatInfoService.deleteAllChatInfo(roomId, userDetails);
    }
}
