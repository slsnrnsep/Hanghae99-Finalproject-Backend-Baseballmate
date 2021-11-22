package com.finalproject.backend.baseballmate.join;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestsRepository  extends JpaRepository<JoinRequests,Long> {
        JoinRequests findByUserIdAndPostId(Long userid,Long postId);
        List<JoinRequests> findByOwnUserId(Long ownUserId);
        List<JoinRequests> findByUserId(Long userId);
        Optional<JoinRequests> findByIdAndUserId(Long joinId, Long userId);
        void deleteByPostId(Long postId);
        Long countAllByOwnUserId(Long ownUserId);

}

