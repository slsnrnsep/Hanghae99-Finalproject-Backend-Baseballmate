package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ChatMessage;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.finalproject.backend.baseballmate.model.QChatMessage.chatMessage;


@RequiredArgsConstructor
@Repository
public class ChatMessageQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<ChatMessage> findByRoomIdOrderByIdDesc(String roomId, Pageable pageable) {
        QueryResults<ChatMessage> result = queryFactory.selectFrom(chatMessage)
                .where(chatMessage.roomId.eq(roomId))
                .orderBy(chatMessage.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Long countAllByRoomIdAndType(String roomId, ChatMessage.MessageType type) {
        return queryFactory.selectFrom(chatMessage)
                .where(chatMessage.roomId.eq(roomId))
                .where(chatMessage.type.eq(type))
                .fetchJoin()
                .fetchCount();
    }

    public ChatMessage findbyRoomIdAndTalk(String RoomId) {
        return queryFactory.selectFrom(chatMessage)
                .where(chatMessage.roomId.eq(RoomId))
                .where(chatMessage.type.eq(ChatMessage.MessageType.TALK))
                .orderBy(chatMessage.id.desc())
                .fetchFirst();
    }
}
