package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    CommunityComment findByCommunityCommentId(Long communityCommentId);
}
