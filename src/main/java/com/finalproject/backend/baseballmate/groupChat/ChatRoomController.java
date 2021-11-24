package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
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
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 채팅방 만들기(모임 확정지은 모임장만 가능)
    @PostMapping("/chat/{groupId}")
    public ChatRoomCreateResponseDto createRoom(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        ChatRoomCreateResponseDto chatRoomCreateResponseDto = chatRoomService.createChatRoom(groupId, user);
        return chatRoomCreateResponseDto;
    }
    // 채팅방 만들기(모임 확정지은 모임장만 가능)
    @PostMapping("/chat/screen/{screenId}")
    public ChatRoomCreateResponseDto createRoom2(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        ChatRoomCreateResponseDto chatRoomCreateResponseDto = chatRoomService.createChatRoom2(screenId, user);
        return chatRoomCreateResponseDto;
    }

    // 사용자별 채팅방 목록 조회
    @ApiOperation(value = "사용자별 채팅방 목록 조회", notes = "사용자별 채팅방 목록 조회")
    @GetMapping("/chat/rooms/mine")
    public List<ChatRoomResponseDto> getOnesChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getOnesChatRoom(userDetails.getUser());
    }


    // 해당 채팅방의 메세지 조회
    @ApiOperation(value = "해당 채팅방의 메세지 조회", notes = "해당 채팅방의 메세지 조회")
    @GetMapping("/chat/{roomId}/messages")
    public Page<ChatMessage> getRoomMessage(@PathVariable String roomId, @PageableDefault Pageable pageable) {
        return chatMessageService.getChatMessageByRoomId(roomId, pageable);
    }

    @ApiOperation(value = "해당 채팅방 나가기", notes = "해당 채팅방 나가기")
    @DeleteMapping("/chat/quit/{groupId}")
    public void quitChat(@PathVariable Long groupId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.quitChat(groupId,userDetails);
    }

    @DeleteMapping("/chat/screen/quit/{screenId}")
    public void quitChat2(@PathVariable Long screenId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.quitChat2(screenId,userDetails);
    }

}
