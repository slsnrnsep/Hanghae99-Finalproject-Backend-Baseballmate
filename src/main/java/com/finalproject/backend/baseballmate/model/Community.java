package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    @Column(nullable = false,length = 1000)
    private String content; // 게시글 내용

    @Column
    private String communityUserPicture;

    @Column
    private String filePath;

    @Column
    private String myTeam;

    @Column
    private Long userId;

    @Column
    private String usertype;
    //종아요
    @JsonManagedReference
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
    @JsonManagedReference
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> communityCommentList = new ArrayList<>();

    public Community(User loginedUser, CommunityRequestDto requestDto, String communityUserPicture, String myTeam,Long userId, String usertype){
        this.createdUser = loginedUser;
        this.userName = loginedUser.getUsername();
        this.content = requestDto.getContent();
        this.filePath = requestDto.getFilePath();
        this.myTeam = myTeam;
        this.communityUserPicture = communityUserPicture;
        this.userId = userId;
        this.usertype = usertype;
    }

    public void updateCommunity(CommunityRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
