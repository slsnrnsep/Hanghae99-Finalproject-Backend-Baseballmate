package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Alarm;
import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserIdOrderByCreatedAtDesc(User user);
    Integer countAlarmByUserIdAndAlarmStatus(Long userId, Boolean alarmStatus);
}
