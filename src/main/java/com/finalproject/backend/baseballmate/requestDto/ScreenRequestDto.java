package com.finalproject.backend.baseballmate.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRequestDto {
    private String title;
    private String content;
    private int peopleLimit;
    private String groupDate;
    private String filePath;
    private String selectPlace;
}
