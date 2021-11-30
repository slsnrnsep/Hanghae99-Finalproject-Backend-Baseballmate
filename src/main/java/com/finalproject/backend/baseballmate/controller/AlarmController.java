package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Alarm;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"6. 알람"}) // Swagger
public class AlarmController {

    private final AlarmService alarmService;

    // 알람 조회
    @ApiOperation(value = "알람 조회", notes = "토큰으로 해당유저의 알람을 조회합니다.")
    @GetMapping("/alarm")
    public List<Alarm> getAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return alarmService.getAlarm(userDetails);
    }

    // 알람 삭제
    @ApiOperation(value = "알람 삭제", notes = "채팅방아이디로 채팅방안의 유저목록을 조회합니다.")
    @DeleteMapping("/alarm/{alarmId}")
    public MsgResponseDto deleteAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("alarmId") Long alarmId) {
        return alarmService.deleteAlarm(userDetails, alarmId);
    }

    // 안읽은 알람 카운트
    @ApiOperation(value = "안읽은 알람을 카운트", notes = "채팅방아이디로 채팅방안의 유저목록을 조회합니다.")
    @GetMapping("/alarm/count")
    public Integer unreadAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return alarmService.unreadAlarm(userDetails);
    }
}
