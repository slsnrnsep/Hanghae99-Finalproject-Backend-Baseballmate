package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeLineLikesRepository extends JpaRepository<TimeLineLikes,Long> {
    Optional<TimeLineLikes> findByTimeLineIdAndUserId(Long timeLineId, Long UserId);
    List<TimeLineLikes> findAllByUserId(Long UserId);
}
