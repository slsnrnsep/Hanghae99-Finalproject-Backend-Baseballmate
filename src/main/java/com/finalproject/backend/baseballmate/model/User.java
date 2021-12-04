package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String userid; // 유저 이메일 아이디

    @Column(nullable = false, unique = false)
    private String username;

    @JsonIgnore
    private String password;

    private String myselectTeam;

    @JsonIgnore
    private int ranNum;

    @JsonIgnore
    @Column
    private String phoneNumber;

    // 참여 신청한 모임
//    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "appliedGroup",cascade = CascadeType.ALL) // groupapplication을 역참조하여 내가 참여 신청한 모임들 리스트 가져오기
    private List<GroupApplication> appliedGroup = new ArrayList<>();
    // 최종 타입이 groupapplication이 아니고 group이어야 할텐데 함수를 써서 가져오기

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "canceledGroup", cascade = CascadeType.ALL)
    private List<CanceledList> canceledLists = new ArrayList<>();

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.ALL)
    private List<Group> createdGroupList = new ArrayList<>();

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "appliedUser",cascade = CascadeType.ALL)
    private List<ScreenApplication> appliedSecreen = new ArrayList<>();

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "screenCreatedUser", cascade = CascadeType.ALL)
    private List<Screen> createdScreenList = new ArrayList<>();

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "cancledScreen", cascade = CascadeType.ALL)
    private List<CanceledScreenList> canceledScreenLists = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "enteredUser", cascade = CascadeType.ALL)
    private List<AllChatInfo> allChatInfoList = new ArrayList<>();

//    @JsonManagedReference
//    @JsonIgnore
//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @JsonIgnore
    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true, name = "PICTURE")
    private String picture;

    // 마이페이지에서 수정 가능한 정보
    @Column(nullable = true)
    private String selfIntroduction;

    // 마이페이지에서 수정 가능한 정보
    @Column(nullable = true)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<TimeLineLikes> timeLineLikes = new ArrayList<>();

    public void addLikes(TimeLineLikes likes) {this.timeLineLikes.add(likes);}

    public void deleteLikes(TimeLineLikes likes) {this.timeLineLikes.remove(likes);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<GroupLikes> groupLikes = new ArrayList<>();

    public void addGroupLikes(GroupLikes likes) {this.groupLikes.add(likes);}

    public void deleteGroupLikes(GroupLikes likes) {this.groupLikes.remove(likes);}

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<GroupCommentLikes> groupCommentLikes = new ArrayList<>();

    public void addGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.add(likes);}

    public void deleteGroupCommentLikes(GroupCommentLikes likes) {this.groupCommentLikes.remove(likes);}


    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<ScreenLikes> screenLikes = new ArrayList<>();

    public void addScreenLikes(ScreenLikes likes){this.screenLikes.add(likes);}

    public void deleteScreenLikes(ScreenLikes likes){this.screenLikes.remove(likes);}

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<ScreenCommentLikes> screenCommentLikes = new ArrayList<>();

    public void addScreenCommentLikes(ScreenCommentLikes likes){this.screenCommentLikes.add(likes);}

    public void deleteScreenCommentLikes(ScreenCommentLikes likes){this.screenCommentLikes.remove(likes);}

    // 커뮤니티 종하요 생성자
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<CommunityLikes> communityLikes = new ArrayList<>();

    public void addCommunityLikes(CommunityLikes likes) {this.communityLikes.add(likes);}

    public void deleteCommunityLikes(CommunityLikes likes) {this.communityLikes.remove(likes);}

    // goods 좋아요 생성자
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<GoodsLikes> goodsLikes = new ArrayList<>();

    public void addGoodsLikes(GoodsLikes likes){
        this.goodsLikes.add(likes);
    }

    public void deleteGoodsLikes(GoodsLikes likes){
        this.goodsLikes.remove(likes);
    }

    @JsonIgnore
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

    // 로컬에서 강제로 DB에 집어넣는 생성자
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
