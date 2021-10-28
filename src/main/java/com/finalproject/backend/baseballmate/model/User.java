package com.finalproject.backend.baseballmate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userid;

    @Column(nullable = false, unique = false)
    private String username;

    private String password;

    private String myselectTeam;
//    private test test;
//    @OneToMany(mappedBy = "user")
//    private List<Group> groupList = new ArrayList<Group>();

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true, name = "PICTURE")
    private String picture;

//    @Column(nullable = true, name = "EMAIL")
//    private String email;
//
//    @Column(nullable = true, name = "NICKNAME")
//    private String nickname;

    @OneToMany(mappedBy = "user")
    private final List<TimeLineLikes> likes = new ArrayList<>();

    public void addLikes(TimeLineLikes likes) {
        this.likes.add(likes);
    }

    public void deleteLikes(TimeLineLikes likes) {
        this.likes.remove(likes);
    }

    public User(String userid, String username, String password){
        this.userid = userid;
        this.username = username;
        this.password = password;
    }

    // 카카오 로그인에 필요한 생성자
    @Builder
    public User(String username ,String userid, String picture, String password, Long kakaoId){
        this.username = username;
        this.userid = userid;
        this.picture = picture;
        this.password = password;
        this.kakaoId = kakaoId;
    }


}
