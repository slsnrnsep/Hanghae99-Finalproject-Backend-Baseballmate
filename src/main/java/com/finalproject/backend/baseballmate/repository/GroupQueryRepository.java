package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.QGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.finalproject.backend.baseballmate.model.QGroup.*;

@RequiredArgsConstructor
@Repository
public class GroupQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Group findAllGroups(Long GroupId){
        return queryFactory.selectFrom(group)
                .where(group.groupId.eq(GroupId))
                .join(group.chatRoom)
                .join(group.createdUser)
                .fetchJoin()
                .fetchOne();
    }
}
