package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Alarm;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    // 알람조회
    @GetMapping("/alarm")
    public List<Alarm> getAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        // User loginUser = userDetails.getUser();
        return alarmService.getAlarm(userDetails.getUser());
    }

    // 알람삭제
    @DeleteMapping("/alarm")
    public Map<String, String> deleteAlarm(UserDetailsImpl userDetails, @PathVariable("alarmId") Long alarmId){
        return alarmService.deleteAlarm(userDetails, alarmId);
    }

    // 안읽은 알람 카운트
    @GetMapping("/alarm/count")
    public Integer unreadAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return alarmService.unreadAlarm(userDetails);
    }
}
