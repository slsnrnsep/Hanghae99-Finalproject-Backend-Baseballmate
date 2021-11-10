package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findAllByOrderByCreatedAtDesc();
    Screen findByScreenId(Long screenId);
}
