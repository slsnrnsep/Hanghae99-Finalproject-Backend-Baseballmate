package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.CommunityCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class CommunityComment extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long commentId;

    @Column
    private String commentUsername;

    @Column
    private String comment;

    @Column
    private Long commentUserIndex;

    @Column
    private String commentUserId;

    @Column
    private String commentUserPicture;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communityId")
    private Community community;

    public CommunityComment(CommunityCommentRequestDto communityCommentRequestDto, Community community,Long loginedUserIndex, String loginedUserId, String commentUsername, String loginedUserPicture ){
        this.community = community;
        this.commentUserIndex = loginedUserIndex;
        this.commentUserId = loginedUserId;
        this.commentUsername = commentUsername;
        this.commentUserPicture = loginedUserPicture;
        this.comment = communityCommentRequestDto.getComment();
    }

    public void updateCommunityComment(CommunityCommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
