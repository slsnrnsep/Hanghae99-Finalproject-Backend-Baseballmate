package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends Timestamped_12H {
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

//    @ManyToOne
//    @JoinColumn(name = "ChatSenderUserInx")
//    private User sender; // 메시지 보낸 유저의 인덱스

    @Column
    private Long senderId;

    @Column
    private String senderName; // 메시지 보낸 사람의 이름

    @Column
    private String senderImage; // 메시지 보낸 사람의 프로필사진

    @Column
    private String message; // 메시지

    @Builder
    public ChatMessage(MessageType type, String roomId, String message, User sender) {
        this.type = type;
        this.roomId = roomId;
        this.senderId = sender.getId();
        this.senderName = sender.getUsername();
        this.senderImage = sender.getPicture();
        this.message = message;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, User sender) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.senderId =  chatMessageRequestDto.getSenderId();
        this.senderName = sender.getUsername();
        this.senderImage = sender.getPicture();
        this.message = chatMessageRequestDto.getMessage();
    }

}
