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

    @Column
    private String phoneNumber;

//    private test test;
//    @OneToMany(mappedBy = "user")
//    private List<Group> groupList = new ArrayList<Group>();

    // 참여 신청한 모임
    @JsonManagedReference
    @OneToMany(mappedBy = "appliedGroup",cascade = CascadeType.ALL) // groupapplication을 역참조하여 내가 참여 신청한 모임들 리스트 가져오기
    private List<GroupApplication> appliedGroup = new ArrayList<>();
    // 최종 타입이 groupapplication이 아니고 group이어야 할텐데 함수를 써서 가져오기

    @JsonManagedReference
    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.ALL)
    private List<Group> createdGroupList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "screenCreatedUser", cascade = CascadeType.ALL)
    private List<Screen> createdScreenList = new ArrayList<>();

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true, name = "PICTURE")
    private String picture;

//    @Column(nullable = true, name = "EMAIL")
//    private String email;
//
    // 마이페이지에서 수정 가능한 정보
    @Column(nullable = true)
    private String selfIntroduction;

    // 마이페이지에서 수정 가능한 정보
    @Column(nullable = true)
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<TimeLineLikes> timeLineLikes = new ArrayList<>();

    public void addLikes(TimeLineLikes likes) {this.timeLineLikes.add(likes);}

    public void deleteLikes(TimeLineLikes likes) {this.timeLineLikes.remove(likes);
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<GroupLikes> groupLikes = new ArrayList<>();

    public void addGroupLikes(GroupLikes likes) {this.groupLikes.add(likes);}

    public void deleteGroupLikes(GroupLikes likes) {this.groupLikes.remove(likes);}

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<GroupCommentLikes> groupCommentLikes = new ArrayList<>();

    public void addGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.add(likes);}

    public void deleteGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.remove(likes);}

    // goods 좋아요 생성자
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<GoodsLikes> goodsLikes = new ArrayList<>();

    public void addGoodsLikes(GoodsLikes likes){
        this.goodsLikes.add(likes);
    }

    public void deleteGoodsLikes(GoodsLikes likes){
        this.goodsLikes.remove(likes);
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<GoodsCommentLikes> goodsCommentLikes = new ArrayList<>();

    public void addGoodsCommentLikes(GoodsCommentLikes likes){this.goodsCommentLikes.add(likes);}

    public void deleteGoodsCommentLikes(GoodsCommentLikes likes){this.goodsCommentLikes.remove(likes);}



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
