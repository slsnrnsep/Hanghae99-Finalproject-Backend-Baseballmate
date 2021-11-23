package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class ScreenChatMessageController {
    private final ScreenChatMessageService screenChatMessageService;
    private final UserService userService;

    @MessageMapping("/screenMessage")
    public void message(@RequestBody ScreenChatMessageRequestDto screenChatMessageRequestDto) {
        User sender =  userService.getUser(screenChatMessageRequestDto.getSenderId()); //getuser querydsl로 찾아오도록 바꾸기
        ScreenChatMessage screenChatMessage = new ScreenChatMessage(screenChatMessageRequestDto, sender);
        screenChatMessageService.sendScreenChatMessage(screenChatMessage);
    }
}
