package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeLineLikesRepository extends JpaRepository<TimeLineLikes,Long> {
    Optional<TimeLineLikes> findByTimeLineIdAndUserId(Long timeLineId, String UserId);

}
