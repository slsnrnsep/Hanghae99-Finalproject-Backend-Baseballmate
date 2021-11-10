//package com.finalproject.backend.baseballmate.chat;
//
//import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@Api(tags = {"4. 채팅방 유저목록"}) // Swagger
//public class AllChatInfoController {
//
//    private final AllChatInfoService allChatInfoService;
//
//    @ApiOperation(value = "채팅방 유저목록", notes = "채팅방 유저목록")
//    @GetMapping("/chat/user/{roomId}")
//    public List<User> getUser(@PathVariable Long roomId){
//        return allChatInfoService.getUser(roomId);
//    }
//
//    @ApiOperation(value = "채팅방 유저목록", notes = "채팅방 유저목록")
//    @DeleteMapping("/chat/user/{roomId}")
//    public void deleteAllChatInfo(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        allChatInfoService.deleteAllChatInfo(roomId, userDetails);
//    }