package com.finalproject.backend.baseballmate.requestDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupRequestDto {
    private String title;
    private String content;
    private int peopleLimit;
    private String groupDate;
    private String filePath;
    private String seleceTeam;
    // 타입 바뀔수도 있음
}
