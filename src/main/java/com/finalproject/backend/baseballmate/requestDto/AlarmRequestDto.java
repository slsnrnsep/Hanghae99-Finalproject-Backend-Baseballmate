package com.finalproject.backend.baseballmate.requestDto;

import com.finalproject.backend.baseballmate.join.JoinRequests;
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
    private Long joinRequestId;
}
