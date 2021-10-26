package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Group {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long groupId; // 모임 고유번호, pk


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private String username;

    @Column
    private String title; // 모임글의 제목

    @Column
    private String content; // 모임글의 내용

    @Column
    private int peopleLimit; // 모임 최대 제한인원

    @Column
    private String groupDate; // 모임 날짜

    @Column
    private List<GroupComment> groupCommentList; // 모임에 달린 댓글 리스트

//    @Column
//    private String baseballTeam; // 구단 이름

    // 모임글 등록 생성자
    public Group(GroupRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.peopleLimit = requestDto.getPeopleLimit();
        this.groupDate = requestDto.getGroupDate();
        this.username = username;
    }
}
