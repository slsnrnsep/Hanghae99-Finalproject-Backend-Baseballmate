package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ScreenComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenCommentRepository extends JpaRepository<ScreenComment, Long> {
    List<ScreenComment> findAllByScreenScreenIdOrderByModifiedAtDesc(Long screenId);
    ScreenComment findByScreenCommentId(Long screenCommentId);
}
