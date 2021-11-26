package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Goods extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goods_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser")
    private User createdUser;

    @Column
    private String userName; // 굿즈 게시글 작성자의 닉네임

    @Column
    private String goodsName;

    @Column(length = 1000)
    private String goodsContent;

    @Column
    private String filePath;

    @JsonManagedReference
    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<GoodsLikes> likesList;

    @Column(columnDefinition = "integer default 0")
    private int likeCount;

    @Column
    private String goodsUserPicture;

    @Column
    private String myTeam;

    @Column
    private String userAddress;


    @Column
    private Long userId;

    @Column
    private String usertype;

    // comment와 연관관계
    @JsonManagedReference
    @OneToMany(mappedBy = "goods", cascade = CascadeType.REMOVE)
    private List<GoodsComment> goodsCommentList = new ArrayList<>();

    // 굿즈 등록 생성자

    public Goods(User loginUser, GoodsRequestDto requestDto, String goodsUserPicture, String myTeam, String userAddress, Long userId, String usertype){

        this.createdUser = loginUser;
        this.userName = loginUser.getUsername();
        this.goodsName = requestDto.getGoodsName();
        this.goodsContent = requestDto.getGoodsContent();
        this.filePath = requestDto.getFilePath();
        this.goodsUserPicture = goodsUserPicture;
        this.myTeam = myTeam;
        this.userAddress = userAddress;
        this.userId = userId;
        this.usertype = usertype;
    }

    // 굿즈 업데이트 생성자
    public void update(GoodsRequestDto requestDto) {
        this.goodsName = requestDto.getGoodsName();
        this.goodsContent = requestDto.getGoodsContent();
    }

    // 좋아요 생성자
    public void addGoodsLikes(GoodsLikes like){
        this.likesList.add(like);
        this.likeCount += 1;
    }
    public void deleteGoodsLikes(GoodsLikes like){
        this.likesList.remove(like);
        this.likeCount -= 1;
    }

}
