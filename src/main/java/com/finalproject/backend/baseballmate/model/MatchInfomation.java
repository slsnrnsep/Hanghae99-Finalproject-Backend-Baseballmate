package com.finalproject.backend.baseballmate.model;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
@Builder
public class MatchInfomation {

    private String date;
    private String time;
    private String match;
    private String location;
    private String homeImage;
    private String awayImage;
}
