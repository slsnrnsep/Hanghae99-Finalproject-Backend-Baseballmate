package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GroupCommentLikes {
    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "groupcomment_id")
    @ManyToOne
    private GroupComment groupComment;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;



    public GroupCommentLikes(GroupComment groupComment,User user)
    {
        this.groupComment = groupComment;
        this.user = user;
    }

}
