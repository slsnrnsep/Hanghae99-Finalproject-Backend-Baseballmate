package com.finalproject.backend.baseballmate.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@ToString
@Getter
@Entity
@NoArgsConstructor
public class MatchInfomation {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long matchId;

    private String date;
    private String time;
    private String match;
    private String hometeam;
    private String awayteam;
    private String location;
    private String homeImage;
    private String awayImage;

    @Builder
    public MatchInfomation(String date ,String time, String match, String hometeam, String awayteam, String location, String homeImage,String awayImage){
        this.date = date;
        this.time = time;
        this.match = match;
        this.hometeam = hometeam;
        this.awayteam = awayteam;
        this.location = location;
        this.homeImage = homeImage;
        this.awayImage = awayImage;
    }

    public void update(String date ,String time, String match, String location, String homeImage,String awayImage) {
        this.date = date;
        this.time = time;
        this.match = match;
        this.location = location;
        this.homeImage = homeImage;
        this.awayImage = awayImage;
    }
}
