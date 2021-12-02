package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Screen extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Column(name = "screen_id")
    private Long screenId; // 스크린 야구모임 고유번호

    // 유저 아이디값이 들어감
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screenCreatedUser")
    private User screenCreatedUser;

    @Column
    private String createdUsername; // 모임 작성자의 유저네임

    @Column
    private String title; // 모임글의 제목

    @Column(length = 1000)
    private String content; // 모임글의 내용

    @Column
    private int peopleLimit; // 모임 최대 제한인원

    @Column
    private String filePath;

    // 스야모임을 취소한 유저 리스트
    @JsonManagedReference
    @OneToMany(mappedBy = "cancledScreen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CanceledScreenList> canceledScreenLists = new ArrayList<>();

    // 참가 신청한 유저와 해당 모임 정보
    @JsonManagedReference
    @OneToMany(mappedBy = "appliedScreen", cascade = CascadeType.ALL)
    private List<ScreenApplication> screenApplications = new ArrayList<>();

//    @JsonManagedReference
//    @OneToMany(mappedBy = "id")
//    private List<User> canceledUser = new ArrayList<>();

    @Column
    private int nowAppliedNum; // 현재 참여신청한 인원 -> get으로 가져오기

    @Column
    private int canApplyNum; // 현재 참여 가능 인원

    @Column
    private double hotPercent; // 모임의 핫한 정도, 기본값 0

    @Column
    private String groupDate; // 모임 날짜

    @Column
    private String selectPlace;

    @Column
    private String placeInfomation;

    @Column
    private boolean allowtype;

    @JsonBackReference
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScreenComment> screenCommentList = new ArrayList<>();

    // 스크린 야구 모임 좋아요
    @JsonManagedReference
    @OneToMany(mappedBy = "screenlikes",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScreenLikes> screenLikesList;

    @Column(columnDefinition = "integer default 0")
    private int screenlikeCount;

    // 스크린모임에 해당하는 채팅방
    @OneToOne(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom screenChatRoom;

    public void addLikes(ScreenLikes like){
        this.screenLikesList.add(like);
        this.screenlikeCount += 1;
    }
    public void deleteLikes(ScreenLikes like){
        this.screenLikesList.remove(like);
        this.screenlikeCount -= 1;
    }


    // 스크린모임 등록 생성자
    public Screen(ScreenRequestDto requestDto, User loginedUser){
        this.screenCreatedUser = loginedUser;
        this.createdUsername = loginedUser.getUsername();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.nowAppliedNum = 0;
        this.canApplyNum = requestDto.getPeopleLimit();
        this.hotPercent = 0;
        this.groupDate = requestDto.getGroupDate();
        this.selectPlace = requestDto.getSelectPlace();
        this.filePath = requestDto.getFilePath();
        this.placeInfomation = requestDto.getPlaceInfomation();
        this.allowtype = true;
    }

    // 스크린모임 수정 메소드
    public void updateScreen(ScreenRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.nowAppliedNum = getNowAppliedNum();
        this.canApplyNum = (requestDto.getPeopleLimit() - getNowAppliedNum());
        this.groupDate = requestDto.getGroupDate();
        this.filePath = requestDto.getFilePath();
    }
}
