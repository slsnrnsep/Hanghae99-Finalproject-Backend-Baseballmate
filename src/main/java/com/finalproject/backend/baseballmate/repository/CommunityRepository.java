package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findAllByOrderByCreatedAtDesc();
    Community findByCommunityId(Long communityId);
}
