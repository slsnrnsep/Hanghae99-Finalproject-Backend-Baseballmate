package com.finalproject.backend.baseballmate.responseDto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Boolean ok;
    private String msg;
    private int status;

    public UserResponseDto(Boolean ok, String msg, int status){
        this.ok = ok;
        this.msg = msg;
        this.status = status;
    }
}
