package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ScreenCommentLikes;
import com.finalproject.backend.baseballmate.model.ScreenLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenCommentLikesRepository extends JpaRepository<ScreenCommentLikes, Long> {
    Optional<ScreenCommentLikes> findByScreenCommentLikesIdAndUserId(Long screenId, Long UserId);
    List<ScreenCommentLikes> findAllByUserId(Long UserId);
}
