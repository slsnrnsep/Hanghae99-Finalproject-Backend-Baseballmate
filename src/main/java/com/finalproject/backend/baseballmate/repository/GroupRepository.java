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
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByOrderByCreatedAtDesc();
    Group findByGroupId(Long groupId);
    List<Group> findTop5ByOrderByHotPercentDesc();
    // 페이지네이션, 모임 생성 시 선택한 구단
    List<Group> findAllBySelectTeamOrderByCreatedAtDesc(String selectTeam, Pageable pageable);
    List<Group> findAllByCreatedUser(User user);

    Page<Group> findBySelectTeam(String selectTeam, Pageable pageable);
}
