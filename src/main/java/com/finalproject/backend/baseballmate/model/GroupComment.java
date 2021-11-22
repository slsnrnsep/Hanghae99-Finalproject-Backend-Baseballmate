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
public class GroupComment extends Timestamped{

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

    @Column
    private String commentUserPicture;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;

    @JsonBackReference // jsonmanagereference로 바꾸기
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

    // 댓글 생성 시 사용하는 생성자
    public GroupComment(GroupCommentRequestDto groupCommentRequestDto, Group group, Long loginedUserIndex, String loginedUserId, String commentUsername, String loginedUserPicture) {
        this.group = group;
        this.commentUserIndex = loginedUserIndex;
        this.commentUserId = loginedUserId;
        this.commentUsername = commentUsername;
        this.commentUserPicture = loginedUserPicture;
        this.comment = groupCommentRequestDto.getComment();
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
