package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
