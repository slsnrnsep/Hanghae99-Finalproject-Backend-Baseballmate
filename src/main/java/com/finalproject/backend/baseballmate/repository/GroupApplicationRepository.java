package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GroupApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Long>{
    // 모임 ID가 같은 행을 리스트로 추출
//    Iterable<GroupApplication> findAllByAppliedGroup_GroupId (Long groupId);
    List<GroupApplication> findAll();

//    GroupApplication findByAppliedGroupGroupIdAAndAppliedUserId(Long appliedGroupId,Long userId);
    GroupApplication findByAppliedGroup_GroupIdAndAppliedUserId(Long appliedGroupId,Long userId);
}
