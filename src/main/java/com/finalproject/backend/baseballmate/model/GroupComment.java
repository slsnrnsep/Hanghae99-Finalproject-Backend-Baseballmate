package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
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
    private Long commentUserIndex;

    @Column
    private String commentUserId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "commentUserId")
//    private User commentUser;

    public GroupComment(GroupCommentRequestDto groupCommentRequestDto, Group group, String commentUsername, Long loginedUserIndex, String loginedUserId) {
        this.commentUsername = commentUsername;
        this.comment = groupCommentRequestDto.getComment();
        this.group = group;
        this.commentUserIndex = loginedUserIndex;
        this.commentUserId = loginedUserId;
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
