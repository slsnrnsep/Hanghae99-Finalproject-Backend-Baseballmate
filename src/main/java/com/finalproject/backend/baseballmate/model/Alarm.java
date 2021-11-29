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
public class Alarm extends Timestamped_12H{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    // 유저 아이디값이 들어감
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser")
    private User alarmCreatedUser;

    @Column
    private String contents;

    @JsonIgnore
    @Column
    private Boolean alarmStatus;

    private Long joinRequestId;

    private String alarmType;

    private Long postId;

    private String normalType;

    public Alarm(AlarmRequestDto alarmRequestDto){
        this.userId = alarmRequestDto.getUserId();
        this.contents = alarmRequestDto.getContents();
        this.alarmStatus = false;
        this.joinRequestId = alarmRequestDto.getJoinRequestId();
        this.postId = alarmRequestDto.getPostId();
        this.alarmType = alarmRequestDto.getAlarmType();
        this.normalType = alarmRequestDto.getNormalType();

    }

    public void updateAlarm(AlarmSaveDto alarmSaveDto) {
        this.alarmStatus = alarmSaveDto.getAlarmStatus();
    }
}
