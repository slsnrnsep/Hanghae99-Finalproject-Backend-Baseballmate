package com.finalproject.backend.baseballmate.screenChat;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.finalproject.backend.baseballmate.screenChat.QScreenChatMessage.screenChatMessage;

@RequiredArgsConstructor
@Repository
public class ScreenChatMessageQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<ScreenChatMessage> findByRoomIdOrderByIdDesc(String roomId, Pageable pageable) {
        QueryResults<ScreenChatMessage> result = queryFactory.selectFrom(screenChatMessage)
                .where(screenChatMessage.roomId.eq(roomId))
                .orderBy(screenChatMessage.id.desc())
//                .join(chatMessage.sender)
//                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Long countAllByRoomIdAndType(String roomId, ScreenChatMessage.MessageType type) {
        return queryFactory.selectFrom(screenChatMessage)
                .where(screenChatMessage.roomId.eq(roomId))
                .where(screenChatMessage.type.eq(type))
//                .join(chatMessage.sender)
                .fetchJoin()
                .fetchCount();
    }

    public ScreenChatMessage findByRoomIdAndTalk(String RoomId) {
        return queryFactory.selectFrom(screenChatMessage)
                .where(screenChatMessage.roomId.eq(RoomId))
                .where(screenChatMessage.type.eq(ScreenChatMessage.MessageType.TALK))
                .orderBy(screenChatMessage.id.desc())
                .fetchFirst();
    }
}
