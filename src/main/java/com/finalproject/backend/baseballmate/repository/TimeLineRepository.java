package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.TimeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLineRepository extends JpaRepository<TimeLine,Long> {
    List<TimeLine> findAllByOrderByCreatedAtDesc();
}
