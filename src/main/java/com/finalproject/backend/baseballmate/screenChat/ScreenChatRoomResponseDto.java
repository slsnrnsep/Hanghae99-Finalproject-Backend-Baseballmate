package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.Screen;
import lombok.Getter;

@Getter
public class ScreenChatRoomResponseDto {
    private final String title;
    private final Long ownUserId;
    private final Long roomId;
    private final Long screenId;
    private final Long headCountChat;
    private final boolean chatValid;
    private final boolean newMessage;

    public ScreenChatRoomResponseDto(ScreenChatRoom screenChatRoom, Screen screen, Long headCountChat, boolean newMessage) {
        this.title = screen.getTitle();
        this.ownUserId = screenChatRoom.getOwnUserId();
        this.roomId = screenChatRoom.getId();
        this.screenId = screen.getScreenId();
        this.headCountChat = headCountChat;
        this.chatValid = screenChatRoom.isChatValid();
        this.newMessage = newMessage;
    }
}
