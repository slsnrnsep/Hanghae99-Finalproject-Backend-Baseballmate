package com.finalproject.backend.baseballmate.repository;
import com.finalproject.backend.baseballmate.model.Group;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.finalproject.backend.baseballmate.model.QGroup.*;


public class GroupRepositoryImpl implements GroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GroupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }



    @Override
    public List<Group> searchTeamHotgroup(String myteam) {
        return queryFactory
                .selectFrom(group)
                .where(group.selectTeam.eq(myteam))
                .orderBy(group.hotPercent.desc())
                .fetch();
    }
}
