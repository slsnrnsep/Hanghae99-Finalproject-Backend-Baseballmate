package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HotGroupResponseDto {
    private String createdUsername; // 모임 형성한 유저의 유저네임
    private String title; // 모임 게시글의 제목
    private int peopleLimit; // 모임 최대 제한 인원 수
    private int canApplyNum; // 현재 참여 가능한 인원 수
    private double hotPercent; // 현재 모임의 인기도
    private String stadium; // 경기장 이름
    private String groupDate; // 모임 날짜

    //프로필 사진
    //등급정보
    //모집중인지 아닌지
    //D-10일 남았는지
}
