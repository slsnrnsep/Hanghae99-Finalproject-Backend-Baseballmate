package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    CommunityComment findByCommunityCommentId(Long communityCommentId);
    List<CommunityComment> findAllByCommunity_CommunityIdOrderByModifiedAtDesc(Long communityId);

//    List<CommunityComment> countAllByCommunityCommentId(Long communityCommentId);

}
