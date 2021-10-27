package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Group_table")
public class Group extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long groupId; // 모임 고유번호, pk

    @Column
    private String madeUser; // 모임 작성자의 유저네임

    @Column
    private String title; // 모임글의 제목

    @Column
    private String content; // 모임글의 내용

    @Column
    private int peopleLimit; // 모임 최대 제한인원

    @Column
    private int nowApplyNum; // 현재 참여신청한 인원 -> get으로 가져오기

    @Column
    private int canApplyNum; // 현재 참여 가능 인원

    @Column
    private String stadium; // 경기장 이름

    @Column
    private String groupDate; // 모임 날짜

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userIndex")
//    private User userIndex; // user테이블의 id값

//    @Column
//    private List<GroupComment> groupCommentList; // 모임에 달린 댓글 리스트

//    @Column
//    private String baseballTeam; // 구단 이름


    // 모임글 등록 생성자
    public Group(GroupRequestDto requestDto, String madeUser) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.groupDate = requestDto.getGroupDate();
        this.madeUser = madeUser;
    }

//    public Group(GroupRequestDto requestDto, User user) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//        this.peopleLimit = requestDto.getPeopleLimit();
//        this.groupDate = requestDto.getGroupDate();
//        this.user = user;
//    }
}
