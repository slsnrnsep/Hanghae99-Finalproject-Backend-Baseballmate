package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupApplication;
import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Long>{
    // 모임 ID가 같은 행을 리스트로 추출
//    Iterable<GroupApplication> findAllByAppliedGroup_GroupId (Long groupId);
    List<GroupApplication> findAll();
    List<GroupApplication> findAllByAppliedUser(User user);
    List<GroupApplication> findAllByAppliedGroup(Group group);
    GroupApplication findByAppliedGroupAndAppliedUser(Group group, User user);
    GroupApplication findByAppliedGroup_GroupIdAndAppliedUserId(Long appliedGroupId,Long userId);
}
