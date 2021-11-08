package com.finalproject.backend.baseballmate.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhoneResponseDto {
    private String status;
    private String message;

    public PhoneResponseDto(String status, String message){
        this.status = status;
        this.message = message;
    }
}