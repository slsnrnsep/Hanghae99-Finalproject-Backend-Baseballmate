package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.Group;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {
    private final String title;
    private final Long ownUserId;
    private final Long roomId;
    private final Long groupId;
    private final Long headCountChat;
    private final boolean chatValid;
    private final boolean newMessage;
    private final String chatRoomImage;

    public ChatRoomResponseDto(ChatRoom chatRoom, Group group, Long headCountChat, boolean newMessage) {
        this.title = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getGroupId();
        this.headCountChat = headCountChat;
        this.chatValid = chatRoom.isChatValid();
        this.newMessage = newMessage;
        this.chatRoomImage = chatRoom.getChatRoomImage();
    }
}
