package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    CommunityComment findByCommentId(Long commentId);
    List<CommunityComment> findAllByCommunity_CommunityIdOrderByCreatedAtAsc(Long communityId);

//    List<CommunityComment> countAllByCommunityCommentId(Long communityCommentId);

}
