package com.finalproject.backend.baseballmate.screenChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenChatMessageRequestDto {
    private ScreenChatMessage.MessageType type;
    private String roomId;
    private Long senderId;
    private String message;
}
