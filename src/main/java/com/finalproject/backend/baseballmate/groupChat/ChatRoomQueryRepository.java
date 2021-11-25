package com.finalproject.backend.baseballmate.groupChat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.groupChat.QChatRoom.chatRoom;

@RequiredArgsConstructor
@Repository
public class ChatRoomQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findAllByChatValidFalse(){
        return queryFactory.selectFrom(chatRoom)
                .where(chatRoom.chatValid.eq(false))
                .join(chatRoom.groupinx)
                .fetchJoin()
                .fetch();
    }
}
