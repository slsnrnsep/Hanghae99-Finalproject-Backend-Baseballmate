package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
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
@Table(name = "Group_table")
public class Group extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "group_id")
    private Long groupId; // 모임 고유번호, pk

    // 유저 아이디값이 들어감
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser")
    private User createdUser;

    @Column
    private String createdUsername; // 모임 작성자의 유저네임

    @Column
    private String title; // 모임글의 제목

    @Column
    private String content; // 모임글의 내용

    @Column
    private int peopleLimit; // 모임 최대 제한인원

    @Column
    private String filePath;

    // 참가 신청한 유저와 해당 모임 정보
    @JsonManagedReference
    @OneToMany(mappedBy = "appliedGroup",cascade = CascadeType.ALL)
    private List<GroupApplication> groupApplications = new ArrayList<>();
    // groupapplication에서 유저 정보 빼오기

    @Column
    private int nowAppliedNum; // 현재 참여신청한 인원 -> get으로 가져오기

    @Column
    private int canApplyNum; // 현재 참여 가능 인원

    @Column
    private double hotPercent; // 모임의 핫한 정도, 기본값 0

    @Column
    private String stadium; // 경기장 이름

    @Column
    private String groupDate; // 모임 날짜

    @Column
    private String selectTeam;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userIndex")
//    private User userIndex; // user테이블의 id값

//    @Column
//    private String baseballTeam; // 구단 이름

    // 모임을 취소했던 유저 리스트
    @JsonManagedReference
    @OneToMany(mappedBy = "canceledGroup", cascade = CascadeType.ALL)
    private List<CanceledList> canceledLists = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<GroupComment> groupCommentList = new ArrayList<>();

    // 좋아요
    @JsonManagedReference
    @OneToMany(mappedBy = "grouplikes",cascade = CascadeType.ALL)
    private List<GroupLikes> grouplikesList;

    @Column(columnDefinition = "integer default 0")
    private int grouplikeCount;

    public void addLikes(GroupLikes like) {
        this.grouplikesList.add(like);
        this.grouplikeCount += 1;
    }

    public void deleteLikes(GroupLikes like) {
        this.grouplikesList.remove(like);
        this.grouplikeCount -= 1;
    }


    // 모임글 등록 생성자
    public Group(GroupRequestDto requestDto, User loginedUser) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.nowAppliedNum = 0;
        this.canApplyNum = requestDto.getPeopleLimit();
        this.hotPercent = 0;
        this.groupDate = requestDto.getGroupDate();
        this.createdUser = loginedUser;
        this.createdUsername = loginedUser.getUsername();
        this.filePath = requestDto.getFilePath();
        this.selectTeam = requestDto.getSelectTeam();
    }

    // 모임글 수정 메소드
    public void updateGroup(GroupRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.nowAppliedNum = getNowAppliedNum();
        this.canApplyNum = (requestDto.getPeopleLimit() - getNowAppliedNum());
        this.groupDate = requestDto.getGroupDate();
    }

}
