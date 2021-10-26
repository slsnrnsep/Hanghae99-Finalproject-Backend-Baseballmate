package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MsgResponseDto {
    private String status;
    private String message;
}
