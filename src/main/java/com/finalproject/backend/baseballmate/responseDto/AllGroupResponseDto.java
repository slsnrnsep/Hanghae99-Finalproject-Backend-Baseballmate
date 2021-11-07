package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllGroupResponseDto {
    private Long groupId;
    private String title;
    private String createdUsername;
    private int peopleLimit;
    private int canApplyNum;
    private double hotPercent;
    private String stadium;
    private String groupDate;
    private String filePath;
    private String selectTeam;
    // private LocalDateTime createdAt;
}
