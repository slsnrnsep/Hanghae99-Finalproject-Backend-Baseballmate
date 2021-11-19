package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findAllByOrderByCreatedAtDesc();
    Screen findByScreenId(Long screenId);
    List<Screen> findAllByScreenCreatedUser(User user);

    Page<Screen> findByPlaceInfomation(String location, Pageable pageable);
}
