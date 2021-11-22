package com.finalproject.backend.baseballmate.join;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.finalproject.backend.baseballmate.join.QJoinRequests.*;

@Repository
@RequiredArgsConstructor
public class JoinRequestQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existByUserId(Long userId) {
        return (queryFactory.selectFrom(joinRequests)
                .where(joinRequests.ownUserId.eq(userId))
                .fetchFirst() != null);
    }
}
