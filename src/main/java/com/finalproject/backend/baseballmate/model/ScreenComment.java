package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ScreenComment {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long screenCommentId;

    @Column
    private String commentUsername;

    @Column
    private String comment;

    @Column
    private Long commentUserIndex;

    @Column
    private String commentUserId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screenId")
    private Screen screen;

    public ScreenComment(ScreenCommentRequestDto screenCommentRequestDto, Screen screen, String commentUsername, Long loginedUserIndex, String loginedUserId){
        this.commentUsername = commentUsername;
        this.comment = screenCommentRequestDto.getComment();
        this.commentUserIndex = loginedUserIndex;
        this.commentUserId = loginedUserId;
        this.screen = screen;
    }

    public void updateScreenComment(ScreenCommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
