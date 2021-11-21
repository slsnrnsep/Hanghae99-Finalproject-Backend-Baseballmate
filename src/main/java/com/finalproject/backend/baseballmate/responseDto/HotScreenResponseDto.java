package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HotScreenResponseDto {
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
    private String dday;
    private boolean allowtype;
    // private LocalDateTime createdAt;
}
