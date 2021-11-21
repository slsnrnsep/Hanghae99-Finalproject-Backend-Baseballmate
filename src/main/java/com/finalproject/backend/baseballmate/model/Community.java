package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.CommunityRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Community extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "community_id")
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser")
    private User createdUser;// 게시글 작성자의 아이디, 중복 허용X

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false)
    private String content; // 게시글 내용

    @Column
    private String communityUserPicture;

    @Column
    private String filePath;

    @Column
    private String myTeam;

    //종아요
    @JsonBackReference
    @OneToMany(mappedBy = "communitylikes", cascade = CascadeType.ALL)
    private List<CommunityLikes> communityLikesList;

    @Column(columnDefinition = "integer default 0")
    private int communitylikeCount;

    public void addLikes(CommunityLikes like) {
        this.communityLikesList.add(like);
        this.communitylikeCount += 1;
    }

    public void deleteLikes(CommunityLikes like) {
        this.communityLikesList.remove(like);
        this.communitylikeCount -= 1;
    }


    //코멘트
    @JsonBackReference
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommunityComment> communityCommentList = new ArrayList<>();

    public Community(User loginedUser, CommunityRequestDto requestDto, String communityUserPicture){
        this.createdUser = loginedUser;
        this.userName = loginedUser.getUsername();
        this.title = requestDto.getTitle();
        this.content = requestDto.getTitle();
        this.filePath = requestDto.getFilePath();
        this.myTeam = requestDto.getMyTeam();
        this.communityUserPicture = communityUserPicture;
    }

    public void updateCommunity(CommunityRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
