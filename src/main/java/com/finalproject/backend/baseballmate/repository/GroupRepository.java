package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByOrderByCreatedAtDesc();
    Group findByGroupId(Long groupId);
}
