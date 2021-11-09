package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finalproject.backend.baseballmate.requestDto.GoodsCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class GoodsComment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column
    private String userName;

    @Column
    private String comment;

    @Column
    private String commentUserId;

    @Column
    private Long commentUserIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsId")
    private Goods goods;

    @JsonBackReference
    @OneToMany(mappedBy = "goodsComment")
    private List<GoodsCommentLikes> goodsCommentLikesList;

    @Column(columnDefinition = "integer default 0")
    private int goodsCommentlikeCount;

    public void addCommentLikes(GoodsCommentLikes likes){
        this.goodsCommentLikesList.add(likes);
        this.goodsCommentlikeCount += 1;
    }

    public void deleteCommentLikes(GoodsCommentLikes likes){
        this.goodsCommentLikesList.remove(likes);
        this.goodsCommentlikeCount -= 1;
    }

    public GoodsComment(String userName,GoodsCommentRequestDto requestDto, Goods goods,String loginedUserId,Long loginedUserIndex){
        this.userName = userName;
        this.comment = requestDto.getComment();
        this.goods = goods;
        this.commentUserId = loginedUserId;
        this.commentUserIndex = loginedUserIndex;
    }

    public void updateComment(GoodsCommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
