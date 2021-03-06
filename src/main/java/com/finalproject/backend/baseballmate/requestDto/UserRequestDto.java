package com.finalproject.backend.baseballmate.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserRequestDto {

    private String username;
    private String userid;
    private String password;
    private String myteam;
//    private String picture;
    private String phonenumber;
    private int ranNum;

    public UserRequestDto(String username, String userid , String password,String myteam,int ranNum){

        this.username = username;
        this.userid = userid;
        this.password = password;
        this.myteam = myteam;
        this.ranNum = ranNum;
    }

}

