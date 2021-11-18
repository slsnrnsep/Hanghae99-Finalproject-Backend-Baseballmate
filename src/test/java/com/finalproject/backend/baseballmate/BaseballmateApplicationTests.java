//package com.finalproject.backend.baseballmate;
//
//import com.finalproject.backend.baseballmate.model.Group;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import java.util.List;
//
//import static com.finalproject.backend.baseballmate.model.QGroup.group;
//
//
//@SpringBootTest
//@Transactional
//class BaseballmateApplicationTests {
//
////    @PersistenceContext
////    EntityManager em;
////
////    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
////
////    @Test
////    List<Group> contextLoads() {
////        return queryFactory
////                .selectFrom(group)
////                .where(group.selectTeam.eq("두산"))
////                .orderBy(group.hotPercent.desc())
////                .fetch();
////    }
//
//}
