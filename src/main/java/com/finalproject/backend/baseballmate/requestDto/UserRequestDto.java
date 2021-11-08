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
<<<<<<< HEAD
//    private String picture;
=======
    private String phonenumber;
>>>>>>> d0a864a9515858a3a05975833100e0376c543e60

    public UserRequestDto(String username, String userid , String password,String myteam){

        this.username = username;
        this.userid = userid;
        this.password = password;
        this.myteam = myteam;
    }

}

