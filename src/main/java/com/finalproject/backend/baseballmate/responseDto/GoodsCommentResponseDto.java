package com.finalproject.backend.baseballmate.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoodsCommentResponseDto {
    private String status;
    private String message;

    public GoodsCommentResponseDto(String status, String message){
        this.status = status;
        this.message = message;
    }
}
