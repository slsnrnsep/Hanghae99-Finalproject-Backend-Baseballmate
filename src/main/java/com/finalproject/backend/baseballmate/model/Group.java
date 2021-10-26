package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Group {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long groupId; // 모임 고유번호, pk

    @Column
    private String title; // 모임글의 제목

    @Column
    private String content; // 모임글의 내용

    @Column
    private Long peopleLimit; // 모임 최대 제한인원

    @Column
    private LocalDateTime groupDate; // 모임 날짜

    @Column
    private List<GroupComment> groupCommentList; // 모임에 달린 댓글 리스트

    @Column
    private String baseballTeam; // 구단 이름
}
