package com.finalproject.backend.baseballmate.screenChat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.screenChat.QScreenChatRoom.screenChatRoom;

@RequiredArgsConstructor
@Repository
public class ScreenChatRoomQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ScreenChatRoom> findAllByChatValidFalse(){
        return queryFactory.selectFrom(screenChatRoom)
                .where(screenChatRoom.chatValid.eq(false))
                .join(screenChatRoom.screenGroup)
                .fetchJoin()
                .fetch();
    }
}
