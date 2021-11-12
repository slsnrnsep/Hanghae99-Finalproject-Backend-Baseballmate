package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GroupLikes;
import com.finalproject.backend.baseballmate.model.ScreenLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenLikesRepository extends JpaRepository<ScreenLikes, Long> {
    Optional<ScreenLikes> findByScreenLikeIdAndUserId(Long screenId, Long UserId);
    List<ScreenLikes> findAllByUserId(Long UserId);
}
