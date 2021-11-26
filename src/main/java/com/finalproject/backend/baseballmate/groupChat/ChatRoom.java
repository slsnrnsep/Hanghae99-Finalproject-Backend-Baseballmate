package com.finalproject.backend.baseballmate.groupChat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "room_id")
    private Long id;

    @Column
    private String roomUuid;

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
    @JoinColumn(name="GroupID")
    private Group groupinx;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="ScreenID")
    private Screen screeninx;

    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<AllChatInfo> allChatInfoList = new ArrayList<>();

    public ChatRoom(String uuid, Group group, User user) {
        this.roomUuid = uuid;
        this.roomName = group.getTitle();
        this.chatRoomImage = group.getFilePath();
        this.ownUserId = user.getId();
        this.groupinx = group;
        this.chatValid = true;
    }
    public ChatRoom(String uuid, Screen group, User user) {
        this.roomUuid = uuid;
        this.roomName = group.getTitle();
        this.chatRoomImage = group.getFilePath();
        this.ownUserId = user.getId();
        this.screeninx = group;
        this.chatValid = true;
    }
    public void updatechatValid(boolean chatValid) {
        this.chatValid = chatValid;
    }
}
