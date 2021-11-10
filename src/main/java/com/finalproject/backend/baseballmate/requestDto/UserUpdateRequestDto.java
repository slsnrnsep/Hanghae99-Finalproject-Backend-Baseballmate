package com.finalproject.backend.baseballmate.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String username;
    private String password;
    private String myteam;
    private String picture;
    private String selfIntroduction;
    private String address;
}
