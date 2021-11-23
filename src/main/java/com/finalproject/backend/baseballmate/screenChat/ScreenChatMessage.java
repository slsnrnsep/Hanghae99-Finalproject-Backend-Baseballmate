package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.Timestamped;
import com.finalproject.backend.baseballmate.model.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScreenChatMessage extends Timestamped {
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
//    @JoinColumn(name = "ScreenChatSenderUserInx")
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
    public ScreenChatMessage(MessageType type, String roomId, String message, User sender) {
        this.type = type;
        this.roomId = roomId;
        this.senderId = sender.getId();
        this.senderName = sender.getUsername();
        this.senderImage = sender.getPicture();
        this.message = message;
    }

    @Builder
    public ScreenChatMessage(ScreenChatMessageRequestDto screenChatMessageRequestDto, User sender) {
        this.type = screenChatMessageRequestDto.getType();
        this.roomId = screenChatMessageRequestDto.getRoomId();
        this.senderId =  screenChatMessageRequestDto.getSenderId();
        this.senderName = sender.getUsername();
        this.senderImage = sender.getPicture();
        this.message = screenChatMessageRequestDto.getMessage();
    }
}
