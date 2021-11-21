package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, QUIT, BAN, BREAK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MessageType type; // 메시지 타입

    @Column
    private String roomId; // 방번호

    @Column
    private String message; // 메시지

    @ManyToOne
    @JoinColumn(name = "ChatSenderUserInx")
    private User sender; // 메시지 보낸 유저의 인덱스

//    @Column
//    private Long senderId;

    @Column
    private String userEmail; // 메시지 보낸 사람의 이름

    @Builder
    public ChatMessage(MessageType type, String roomId, String message, User sender) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.userEmail = sender.getUserid();
        this.message = message;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, UserService userService) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.sender =  userService.getUser(chatMessageRequestDto.getSenderId());
        this.message = chatMessageRequestDto.getMessage();
    }

}
