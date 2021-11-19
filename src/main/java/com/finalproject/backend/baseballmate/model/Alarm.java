package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.backend.baseballmate.requestDto.AlarmRequestDto;
import com.finalproject.backend.baseballmate.requestDto.AlarmSaveDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String contents;

    @JsonIgnore
    @Column
    private Boolean alarmStatus;

    public Alarm(AlarmRequestDto alarmRequestDto){
        this.userId = alarmRequestDto.getUserId();
        this.contents = alarmRequestDto.getContents();
        this.alarmStatus = false;
    }

    public void updateAlarm(AlarmSaveDto alarmSaveDto) {
        this.alarmStatus = alarmSaveDto.getAlarmStatus();
    }
}
