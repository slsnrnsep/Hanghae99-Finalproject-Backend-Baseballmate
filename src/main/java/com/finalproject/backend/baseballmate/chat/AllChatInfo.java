//package com.finalproject.backend.baseballmate.chat;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class AllChatInfo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//
//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private ChatRoom chatRoom;
//
//    @Column(nullable = false)
//    private Long lastMessageId;
//
//    public AllChatInfo(User user, ChatRoom chatRoom) {
//        this.user = user;
//        this.chatRoom = chatRoom;
//        this.lastMessageId = 0L;
//    }
//
//    public void updateLastMessageId(Long lastMessageId){
//        this.lastMessageId = lastMessageId;
//    }