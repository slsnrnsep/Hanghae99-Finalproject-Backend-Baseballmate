package com.finalproject.backend.baseballmate.requestDto;

import lombok.Getter;

@Getter
public class UserRequestDto {

    private String username;
    private String userid;
    private String password;

    public UserRequestDto(String username, String userid , String password){

        this.username = username;
        this.userid = userid;
        this.password = password;
    }

}
