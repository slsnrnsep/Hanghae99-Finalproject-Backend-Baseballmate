package com.finalproject.backend.baseballmate.screenChat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScreenChatRoom {

    private static final long serialVersionUID = 6494678977089006639L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "RoomId")
    private Long id;

    @Column
    private String screenRoomUuid;

    @Column
    private String roomName;

    @Column
    private String chatRoomImage; // 채팅방 사진

    @Column
    private Long ownUserId; //방장 인덱스

    @Column
    private int userCount; //채팅방 인원수

    @Column
    private boolean chatValid; // 채팅방 이용가능 여부

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="ScreenGroupInx")
    private Screen screenGroup;

    public ScreenChatRoom(String uuid, Screen screenGroup, User user) {
        this.screenRoomUuid = uuid;
        this.roomName = screenGroup.getTitle();
        this.chatRoomImage = screenGroup.getFilePath();
        this.ownUserId = user.getId();
        this.screenGroup = screenGroup;
        this.chatValid = true;
    }

    public void updatechatValid(boolean chatValid) {
        this.chatValid = chatValid;
    }
}
