package com.finalproject.backend.baseballmate.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoodsResponseDto {
    private String status;
    private String message;

    public GoodsResponseDto(String status, String message){
        this.status = status;
        this.message = message;
    }
}
