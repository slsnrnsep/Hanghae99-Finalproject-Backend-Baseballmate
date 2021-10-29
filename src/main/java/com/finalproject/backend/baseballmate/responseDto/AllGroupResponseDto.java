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
    private int peopleLimit;
    private int canApplyNum;
    private double hotPercent;
    private String stadium;
    private String groupDate;
    // private LocalDateTime createdAt;
}
