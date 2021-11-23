package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.groupChat.ChatMessage;
import com.finalproject.backend.baseballmate.groupChat.ChatRoomCreateResponseDto;
import com.finalproject.backend.baseballmate.groupChat.ChatRoomResponseDto;
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
public class ScreenChatRoomController {
    private final ScreenChatRoomService screenChatRoomService;
    private final ScreenChatMessageService screenChatMessageService;

    // 채팅방 만들기(모임 확정지은 모임장만 가능)
    @PostMapping("/screenChat/{screenId}")
    public ScreenChatRoomCreateResponseDto createRoom(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        ScreenChatRoomCreateResponseDto screenChatRoomCreateResponseDto = screenChatRoomService.createChatRoom(screenId, user);
        return screenChatRoomCreateResponseDto;
    }

    // 사용자별 채팅방 목록 조회
    @ApiOperation(value = "사용자별 채팅방 목록 조회", notes = "사용자별 채팅방 목록 조회")
    @GetMapping("/screenChat/rooms/mine")
    public List<ScreenChatRoomResponseDto> getOnesChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenChatRoomService.getOnesChatRoom(userDetails.getUser());
    }

    // 해당 채팅방의 메세지 조회
    @ApiOperation(value = "해당 채팅방의 메세지 조회", notes = "해당 채팅방의 메세지 조회")
    @GetMapping("/screenChat/{roomId}/messages")
    public Page<ScreenChatMessage> getRoomMessage(@PathVariable String roomId, @PageableDefault Pageable pageable) {
        return screenChatMessageService.getScreenChatMessageByRoomId(roomId, pageable);
    }

    @ApiOperation(value = "해당 채팅방 나가기", notes = "해당 채팅방 나가기")
    @DeleteMapping("/screenChat/quit/{screenId}")
    public void quitChat(@PathVariable Long screenId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        screenChatRoomService.quitChat(screenId,userDetails);
    }

}
