package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CommunityLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityLikesRepository extends JpaRepository<CommunityLikes, Long> {
    Optional<CommunityLikes> findByCommunitylikesCommunityIdAndUserId(Long communityId, Long userId);
//    List<CommunityLikes> findAllByUserId(Long userId);
}
