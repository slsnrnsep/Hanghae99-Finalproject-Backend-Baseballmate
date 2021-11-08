package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private int ranNum;

    @Column(nullable = true , unique = true)
    private String phoneNumber;

//    private test test;
//    @OneToMany(mappedBy = "user")
//    private List<Group> groupList = new ArrayList<Group>();

    // 참여 신청한 모임
    @JsonManagedReference
    @OneToMany(mappedBy = "appliedGroup") // groupapplication을 역참조하여 내가 참여 신청한 모임들 리스트 가져오기
    private List<GroupApplication> appliedGroup = new ArrayList<>();
    // 최종 타입이 groupapplication이 아니고 group이어야 할텐데 함수를 써서 가져오기

    @JsonManagedReference
    @OneToMany(mappedBy = "createdUser")
    private List<Group> createdGroupList = new ArrayList<>();

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
    private final List<TimeLineLikes> timeLineLikes = new ArrayList<>();

    public void addLikes(TimeLineLikes likes) {this.timeLineLikes.add(likes);
    }

    public void deleteLikes(TimeLineLikes likes) {
        this.timeLineLikes.remove(likes);
    }

    @OneToMany(mappedBy = "user")
    private final List<GroupLikes> groupLikes = new ArrayList<>();

    public void addGroupLikes(GroupLikes likes) {this.groupLikes.add(likes);}

    public void deleteGroupLikes(GroupLikes likes) {this.groupLikes.remove(likes);}

    @OneToMany(mappedBy = "user")
    private final List<GroupCommentLikes> groupCommentLikes = new ArrayList<>();

    public void addGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.add(likes);}

    public void deleteGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.remove(likes);}

    // goods 좋아요 생성자
    @OneToMany(mappedBy = "user")
    private final List<GoodsLikes> goodsLikes = new ArrayList<>();

    public void addGoodsLikes(GoodsLikes likes){
        this.goodsLikes.add(likes);
    }

    public void deleteGoodsLikes(GoodsLikes likes){
        this.goodsLikes.remove(likes);
    }


    public User(String userid, String username, String password,String phonenumber,int ranNum){
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.phoneNumber = phonenumber;
        this.ranNum = ranNum;
    }
    public User(String userid, String username, String password,String phonenumber){
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.phoneNumber = phonenumber;
    }
    public User(String phonenumber,int ranNum)
    {
        this.phoneNumber = phonenumber;
        this.ranNum = ranNum;
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
