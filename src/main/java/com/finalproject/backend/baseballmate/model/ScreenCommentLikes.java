package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ScreenCommentLikes {
    @GeneratedValue
    @Id
    private Long screenCommentLikesId;

    @JoinColumn(name = "screencomment_id")
    @ManyToOne
    private ScreenComment screenComment;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public ScreenCommentLikes(ScreenComment screenComment, User user){
        this.screenComment = screenComment;
        this.user = user;
    }

}
