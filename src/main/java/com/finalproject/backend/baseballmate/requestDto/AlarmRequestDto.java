package com.finalproject.backend.baseballmate.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRequestDto {
    private Long userId;
    private String contents;
    private Long postId;
    private Long joinRequestId;
    private String alarmType;
    private String normalType;
}
