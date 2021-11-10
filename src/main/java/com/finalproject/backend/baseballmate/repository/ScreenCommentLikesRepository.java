package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ScreenCommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScreenCommentLikesRepository extends JpaRepository<ScreenCommentLikes, Long> {
    Optional<ScreenCommentLikes> findByScreenCommentLikesIdAndUserId(Long screenId, Long UserId);
}
