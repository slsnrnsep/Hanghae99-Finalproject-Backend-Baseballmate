package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.GroupCommentLikes;
import com.finalproject.backend.baseballmate.model.GroupLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupCommentLikesRepository extends JpaRepository<GroupCommentLikes,Long> {
    Optional<GroupCommentLikes> findByGroupcommentIdandUserId(Long groupId, Long UserId);
    List<GroupCommentLikes> findAllByUserId(Long UserId);
}
