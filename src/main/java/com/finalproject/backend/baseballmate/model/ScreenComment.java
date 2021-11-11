package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class ScreenComment extends Timestamped {
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

    @JsonBackReference
    @OneToMany(mappedBy = "screenComment",cascade = CascadeType.ALL)
    private List<ScreenCommentLikes> screenCommentLikesList;

    @Column(columnDefinition = "integer default 0")
    private int screencommentlikeCount;

    public void addLikes(ScreenCommentLikes like){
        this.screenCommentLikesList.add(like);
        this.screencommentlikeCount += 1;
    }
    public void deleteLikes(ScreenCommentLikes like){
        this.screenCommentLikesList.remove(like);
        this.screencommentlikeCount -= 1;
    }


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
