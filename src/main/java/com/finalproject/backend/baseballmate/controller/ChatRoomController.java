package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.ChatMessage;
import com.finalproject.backend.baseballmate.responseDto.ChatRoomCreateResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ChatRoomResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ChatMessageService;
import com.finalproject.backend.baseballmate.service.ChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"5. 채팅방"}) // Swagger
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 모임 채팅방 만들기(모임 확정지은 모임장만 가능)
    @ApiOperation(value = "직관모임용 채팅방 생성", notes = "토큰과 그룹아이디로 채팅방을 생성합니다.")
    @PostMapping("/chat/{groupId}")
    public ChatRoomCreateResponseDto createGroupRoom(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createGroupChatRoom(groupId, userDetails);
    }

    // 스크린 채팅방 만들기(모임 확정지은 모임장만 가능)
    @ApiOperation(value = "스크린야구용 채팅방 생성", notes = "토큰과 스크린아이디로 채팅방을 생성합니다.")
    @PostMapping("/chat/screen/{screenId}")
    public ChatRoomCreateResponseDto createScreenRoom(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createScreenChatRoom(screenId, userDetails);
    }

    // 사용자별 채팅방 목록 조회
    @ApiOperation(value = "사용자별 채팅방 목록 조회", notes = "토큰을 통해 사용자별 채팅방을 조회합니다.")
    @GetMapping("/chat/rooms/mine")
    public List<ChatRoomResponseDto> getOnesChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getOnesChatRoom(userDetails.getUser());
    }

    // 해당 채팅방의 메세지 조회
    @ApiOperation(value = "해당 채팅방의 메세지 조회", notes = "채팅방 번호를 통해 해당 채팅방 메시지를 조회합니다.")
    @GetMapping("/chat/{roomId}/messages")
    public Page<ChatMessage> getRoomMessage(@PathVariable String roomId, @PageableDefault Pageable pageable) {
        return chatMessageService.getChatMessageByRoomId(roomId, pageable);
    }

    // 직관모임 채팅방 나가기
    @ApiOperation(value = "직관모임 채팅방 나가기", notes = "토큰과 해당 게시글 아이디를 이용해 해당 채팅방 나가기")
    @DeleteMapping("/chat/quit/{groupId}")
    public void quitGroupChat(@PathVariable Long groupId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.quitGroupChat(groupId,userDetails);
    }

    // 스야모임 채팅방 나가기
    @ApiOperation(value = "스크린야구모임 채팅방 나가기", notes = "토큰과 해당 게시글 아이디를 이용해 해당 채팅방 나가기")
    @DeleteMapping("/chat/screen/quit/{screenId}")
    public void quitScreenChat(@PathVariable Long screenId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.quitScreenChat(screenId,userDetails);
    }

    // 채팅 메시지 테스트용 api
    @PostMapping("/test/chat/message")
    public ChatMessage testingChat(@RequestBody String message) {
        return (chatMessageService.testChat(message));
    }
}
