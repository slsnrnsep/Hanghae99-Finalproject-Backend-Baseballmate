package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.Group;
import lombok.Getter;

@Getter
public class ChatRoomCreateResponseDto {
    private final String chatRoomName;
    private final Long ownUserId;
    private final Long roomId;
    private final Long groupId;
    private final boolean chatValid;

    public ChatRoomCreateResponseDto(ChatRoom chatRoom, Group group) {
        this.chatRoomName = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getGroupId();
        this.chatValid = chatRoom.isChatValid();
    }
}
