package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.TimeLine;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TimeLineResponseDto
{
    private String status;
    private String message;

    public TimeLineResponseDto(String status,String msg){
        this.status = status;
        this.message = msg;

    }
}
