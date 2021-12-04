package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>,GroupRepositoryCustom {
    List<Group> findAllByOrderByCreatedAtDesc();
    Group findByGroupId(Long groupId);
    List<Group> findTop5ByOrderByHotPercentDesc();

    //querydsl 사용전 구단별 핫한그룹조회할때 쓴 JPA
    List<Group> findAllBySelectTeamOrderByHotPercentDesc(String selectTeam);

    List<Group> findAllByCreatedUser(User user);

    Page<Group> findBySelectTeam(String selectTeam, Pageable pageable);


}
