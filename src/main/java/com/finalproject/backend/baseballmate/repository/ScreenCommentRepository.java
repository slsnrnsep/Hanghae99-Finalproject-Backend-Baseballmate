package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ScreenComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenCommentRepository extends JpaRepository<ScreenComment, Long> {
    ScreenComment findByScreenCommentId(Long screenCommentId);
}
