package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
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
public class AllScreenChatInfoController {
    private final AllScreenChatInfoService allScreenChatInfoService;

    @ApiOperation(value = "채팅방 유저목록", notes = "채팅방 유저목록")
    @GetMapping("/screenChat/user/{roomId}")
    public List<User> getUser(@PathVariable Long roomId){
        return allScreenChatInfoService.getUser(roomId);
    }

    @ApiOperation(value = "채팅방 유저목록", notes = "채팅방 유저목록")
    @DeleteMapping("/screenChat/user/{roomId}")
    public void deleteAllChatInfo(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        allScreenChatInfoService.deleteAllScreenChatInfo(roomId, userDetails);
    }
}
