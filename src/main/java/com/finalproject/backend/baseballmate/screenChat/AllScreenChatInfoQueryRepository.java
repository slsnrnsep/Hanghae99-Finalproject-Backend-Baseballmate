package com.finalproject.backend.baseballmate.screenChat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.screenChat.QAllScreenChatInfo.allScreenChatInfo;

@RequiredArgsConstructor
@Repository
public class AllScreenChatInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Long countAllByScrenChatRoom(ScreenChatRoom screenChatRoom){
        return queryFactory.selectFrom(allScreenChatInfo)
                .where(allScreenChatInfo.screenChatRoom.eq(screenChatRoom))
                .join(allScreenChatInfo.screenChatRoom)
                .join(allScreenChatInfo.enteredUser)
                .fetchJoin()
                .fetchCount();
    }

    public AllScreenChatInfo findByChatRoom_IdAndUser_Id(Long roomId, Long userId){
        return queryFactory.selectFrom(allScreenChatInfo)
                .where(allScreenChatInfo.screenChatRoom.id.eq(roomId))
                .where(allScreenChatInfo.enteredUser.id.eq(userId))
                .join(allScreenChatInfo.screenChatRoom)
                .join(allScreenChatInfo.enteredUser)
                .fetchJoin()
                .fetchOne();
    }

    public List<AllScreenChatInfo> findAllByChatRoom_Id(Long roomId){
        return queryFactory.selectFrom(allScreenChatInfo)
                .where(allScreenChatInfo.screenChatRoom.id.eq(roomId))
                .join(allScreenChatInfo.screenChatRoom)
                .join(allScreenChatInfo.enteredUser)
                .fetchJoin()
                .fetch();
    }

    public List<AllScreenChatInfo> findAllByUserIdOrderByIdDesc(Long userId){
        return queryFactory.selectFrom(allScreenChatInfo)
                .where(allScreenChatInfo.enteredUser.id.eq(userId))
                .orderBy(allScreenChatInfo.id.desc())
                .join(allScreenChatInfo.screenChatRoom)
                .join(allScreenChatInfo.enteredUser)
                .fetchJoin()
                .fetch();
    }
}
