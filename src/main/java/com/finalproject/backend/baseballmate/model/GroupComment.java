package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "groupComment",cascade = CascadeType.ALL)
    private List<GroupCommentLikes> groupcommentlikesList;

    @Column(columnDefinition = "integer default 0")
    private int groupcommentlikeCount;

    public void addLikes(GroupCommentLikes like) {
        this.groupcommentlikesList.add(like);
        this.groupcommentlikeCount += 1;
    }

    public void deleteLikes(GroupCommentLikes like) {
        this.groupcommentlikesList.remove(like);
        this.groupcommentlikeCount -= 1;
    }
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
