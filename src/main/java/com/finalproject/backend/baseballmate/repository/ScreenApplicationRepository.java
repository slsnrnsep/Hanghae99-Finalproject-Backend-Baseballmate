package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.ScreenApplication;
import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenApplicationRepository extends JpaRepository<ScreenApplication, Long> {
    List<ScreenApplication> findAll();
//    List<ScreenApplication> findAllByAppliedUser(User user);
    ScreenApplication findByAppliedScreenAndAndAppliedUser(Screen screen, User user);
    ScreenApplication findByAppliedScreenScreenIdAndAppliedUserId(Long appliedScreenId,Long userId);
    List<ScreenApplication> findAllByAppliedScreen(Screen screen);
}
