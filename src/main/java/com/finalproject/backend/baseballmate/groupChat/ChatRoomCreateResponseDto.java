package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import lombok.Getter;

@Getter
public class ChatRoomCreateResponseDto {
    private final String chatRoomName;
    private final Long ownUserId;
    private final Long roomId;
    private final Long groupId;
    private final boolean chatValid;
    private final String chatRoomImage;

    public ChatRoomCreateResponseDto(ChatRoom chatRoom, Group group) {
        this.chatRoomName = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getGroupId();
        this.chatValid = chatRoom.isChatValid();
        this.chatRoomImage = chatRoom.getChatRoomImage();
    }

    public ChatRoomCreateResponseDto(ChatRoom chatRoom, Screen group) {
        this.chatRoomName = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getScreenId();
        this.chatValid = chatRoom.isChatValid();
        this.chatRoomImage = chatRoom.getChatRoomImage();
    }
}
