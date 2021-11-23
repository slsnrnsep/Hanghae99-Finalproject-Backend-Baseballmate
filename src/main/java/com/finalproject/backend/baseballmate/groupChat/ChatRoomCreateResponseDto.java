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
<<<<<<< HEAD
    private final String chatRoomIamge;
=======
    private final String chatRoomImage;
>>>>>>> e20c445424f14094f817aa03878b9af877ce0f29

    public ChatRoomCreateResponseDto(ChatRoom chatRoom, Group group) {
        this.chatRoomName = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getGroupId();
        this.chatValid = chatRoom.isChatValid();
<<<<<<< HEAD
        this.chatRoomIamge = chatRoom.getChatRoomImage();
=======
        this.chatRoomImage = chatRoom.getChatRoomImage();
>>>>>>> e20c445424f14094f817aa03878b9af877ce0f29
    }

    public ChatRoomCreateResponseDto(ChatRoom chatRoom, Screen group) {
        this.chatRoomName = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getScreenId();
        this.chatValid = chatRoom.isChatValid();
<<<<<<< HEAD
        this.chatRoomIamge = chatRoom.getChatRoomImage();
=======
        this.chatRoomImage = chatRoom.getChatRoomImage();
>>>>>>> e20c445424f14094f817aa03878b9af877ce0f29
    }
}
