package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.GroupComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupDetailResponseDto {
    private Long groupId;
    private String createdUserName; // 모임 형성한 유저의 유저네임
    private String createdUserId; //  모임 형성한 유저의 이메일 아이디
    private String createdUserProfileImg; // 모임 형성한 유저의 프로필 사진
    private String title; // 모임 게시글의 제목
    private String content; // 모임 게시글의 내용
    private int peopleLimit; // 모임 최대 제한 인원 수
    private int nowAppliedNum; // 현재 참여 신청한 인원 수
    private int canApplyNum; // 현재 참여 가능한 인원 수
    private double hotPercent;
    private String stadium; // 경기장 이름
    private String groupDate; // 모임 날짜
    private String filePath;
    private String dday;
    private List<Map<String, String>> appliedUserInfo;
    List<GroupComment> groupCommentList;

}
