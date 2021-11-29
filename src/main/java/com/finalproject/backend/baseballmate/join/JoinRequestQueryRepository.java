package com.finalproject.backend.baseballmate.join;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.join.QJoinRequests.*;

@Repository
@RequiredArgsConstructor
public class JoinRequestQueryRepository{

    private final JPAQueryFactory queryFactory;

    public boolean existByUserId(Long userId) {
        return (queryFactory.selectFrom(joinRequests)
                .where(joinRequests.ownUserId.eq(userId))
                .fetchFirst() != null);
    }

    public List<JoinRequests> findtypebyUserId(Long userId,String type) {
        return queryFactory.selectFrom(joinRequests)
                .where(joinRequests.ownUserId.eq(userId))
                .where(joinRequests.grouptype.eq(type))
                .orderBy(joinRequests.id.desc())
                .fetch();
    }

    public void findBydeletegroup(Long groupId) {
        queryFactory.delete(joinRequests)
                .where(joinRequests.grouptype.eq("group"))
                .where(joinRequests.postId.eq(groupId))
                .execute();

    }

    public void findBydeletescreen(Long screenId) {
        queryFactory.delete(joinRequests)
                .where(joinRequests.grouptype.eq("screen"))
                .where(joinRequests.postId.eq(screenId))
                .execute();
    }
}
