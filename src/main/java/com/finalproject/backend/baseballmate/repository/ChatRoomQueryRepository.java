package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.model.QChatRoom.chatRoom;

@RequiredArgsConstructor
@Repository
public class ChatRoomQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findAllByChatValidFalse(){
        return queryFactory.selectFrom(chatRoom)
                .where(chatRoom.chatValid.eq(false))
                .join(chatRoom.group)
                .fetchJoin()
                .fetch();
    }
}
