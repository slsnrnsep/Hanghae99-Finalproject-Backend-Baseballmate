package com.finalproject.backend.baseballmate.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class AllChatInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long lastMessageId;

    public AllChatInfo(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.lastMessageId = 0L;
    }

    public void updateLastMessageId(Long lastMessageId){
        this.lastMessageId = lastMessageId;
    }
}
