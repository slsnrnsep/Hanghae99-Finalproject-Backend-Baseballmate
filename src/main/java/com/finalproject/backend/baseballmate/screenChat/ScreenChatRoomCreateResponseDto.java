package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.Screen;
import lombok.Getter;

@Getter
public class ScreenChatRoomCreateResponseDto {
    private final String chatRoomName;
    private final Long ownUserId;
    private final Long roomId;
    private final Long screenId;
    private final boolean chatValid;

    public ScreenChatRoomCreateResponseDto(ScreenChatRoom screenChatRoom, Screen screen) {
        this.chatRoomName = screen.getTitle();
        this.ownUserId = screenChatRoom.getOwnUserId();
        this.roomId = screenChatRoom.getId();
        this.screenId = screen.getScreenId();
        this.chatValid = screenChatRoom.isChatValid();
    }
}
