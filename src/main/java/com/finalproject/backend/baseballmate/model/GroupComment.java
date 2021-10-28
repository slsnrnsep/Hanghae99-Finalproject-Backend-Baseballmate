package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class GroupComment {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long groupCommentId;

    @Column
    private String commentUsername;

    @Column
    private String comment;

    @Column
    private Long commentUserId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "commentUserId")
//    private User commentuser;

    public GroupComment(GroupCommentRequestDto groupCommentRequestDto, Group group, String commentUsername, Long commentUserId) {
        this.commentUsername = commentUsername;
        this.comment = groupCommentRequestDto.getComment();
        this.group = group;
        this.commentUserId = commentUserId;
    }

    public void updateGroupComment(GroupCommentRequestDto requestDto)
    {
        this.comment = requestDto.getComment();
    }

//    public GroupComment(GroupCommentRequestDto groupCommentRequestDto, String username) {
//        this.commentUsername = username;
//        this.comment = groupCommentRequestDto.getComment();
//        this.groupId = groupCommentRequestDto.getGroupId();
//    }
}
