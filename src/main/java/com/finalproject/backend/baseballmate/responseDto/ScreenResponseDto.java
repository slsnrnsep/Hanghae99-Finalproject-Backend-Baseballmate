package com.finalproject.backend.baseballmate.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ScreenResponseDto {
    private String status;
    private String message;

    public ScreenResponseDto(String status, String message){
        this.status = status;
        this.message = message;
    }
}
