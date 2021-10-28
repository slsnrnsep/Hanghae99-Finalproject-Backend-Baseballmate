package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GroupComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupCommentRepository extends JpaRepository<GroupComment,Long> {
//    List<GroupComment> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
}
