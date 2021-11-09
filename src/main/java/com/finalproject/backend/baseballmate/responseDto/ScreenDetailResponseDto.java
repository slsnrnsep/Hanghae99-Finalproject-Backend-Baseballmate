package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScreenDetailResponseDto {
    private Long id;
    private String createdUserName; // 모임 형성한 유저의 유저네임
    private String title; // 모임 게시글의 제목
    private String content; // 모임 게시글의 내용
    private int peopleLimit; // 모임 최대 제한 인원 수
    private int nowAppliedNum; // 현재 참여 신청한 인원 수
    private int canApplyNum; // 현재 참여 가능한 인원 수
    private double hotPercent;
    private String groupDate; // 모임 날짜
//    List<GroupComment> groupCommentList;
    private String filePath;// 모임 게시글 내의 댓글 리스트
}
