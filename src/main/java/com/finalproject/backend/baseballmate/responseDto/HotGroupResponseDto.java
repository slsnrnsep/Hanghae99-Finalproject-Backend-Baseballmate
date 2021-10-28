package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupComment;

import java.util.List;

public class HotGroupResponseDto {
    private String createdUserName; // 모임 형성한 유저의 유저네임
    private String title; // 모임 게시글의 제목
    private int peopleLimit; // 모임 최대 제한 인원 수
    private int canApplyNum; // 현재 참여 가능한 인원 수
    private String stadium; // 경기장 이름
    private String groupDate; // 모임 날짜

    //프로필 사진
    //등급정보
    //모집중인지 아닌지
    //D-10일 남았는지
}
