package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.model.User;
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
        User sender =  userService.getUser(messageRequestDto.getSenderId()); //getuser querydsl로 찾아오도록 바꾸기
        ChatMessage chatMessage = new ChatMessage(messageRequestDto, sender);
        chatMessageService.sendChatMessage(chatMessage);
    }
}
