package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllScreenResponseDto {
    private Long screenId;
    private String title;
    private String createdUsername;
    private int peopleLimit;
    private int canApplyNum;
    private double hotPercent;
    private String groupDate;
    private String filePath;
    private String selectPlace;
    private String placeInfomation;
    private String dday;
}
