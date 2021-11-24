package com.finalproject.backend.baseballmate.groupChat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.groupChat.QAllChatInfo.allChatInfo;

@RequiredArgsConstructor
@Repository
public class AllChatInfoQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Long countAllByChatRoom(ChatRoom chatRoom){
        return queryFactory.selectFrom(allChatInfo)
                .where(allChatInfo.chatRoom.eq(chatRoom))
                .join(allChatInfo.chatRoom)
                .join(allChatInfo.enteredUser)
                .fetchJoin()
                .fetchCount();

    }

    public AllChatInfo findByChatRoom_IdAndUser_Id(Long roomId, Long userId){
        return queryFactory.selectFrom(allChatInfo)
                .where(allChatInfo.chatRoom.id.eq(roomId))
                .where(allChatInfo.enteredUser.id.eq(userId))
                .join(allChatInfo.chatRoom)
                .join(allChatInfo.enteredUser)
                .fetchJoin()
                .fetchOne();

    }

    public List<AllChatInfo> findAllByChatRoom_Id(Long roomId){
        return queryFactory.selectFrom(allChatInfo)
                .where(allChatInfo.chatRoom.id.eq(roomId))
                .join(allChatInfo.chatRoom)
                .join(allChatInfo.enteredUser)
                .fetchJoin()
                .fetch();
    }

    public List<AllChatInfo> findAllByUserIdOrderByIdDesc(Long userId){
        return queryFactory.selectFrom(allChatInfo)
                .where(allChatInfo.enteredUser.id.eq(userId))
                .orderBy(allChatInfo.id.desc())
                .join(allChatInfo.chatRoom)
                .join(allChatInfo.enteredUser)
                .fetchJoin()
                .fetch();
    }
}
