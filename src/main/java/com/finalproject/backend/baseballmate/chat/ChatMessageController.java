package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/message")
    public void message(@RequestBody ChatMessageRequestDto messageRequestDto) {
        ChatMessage chatMessage = new ChatMessage(messageRequestDto, userService);
        chatMessageService.sendChatMessage(chatMessage);
    }
}
