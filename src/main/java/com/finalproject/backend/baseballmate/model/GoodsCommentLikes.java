package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GoodsCommentLikes {
    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "goodscomment_id")
    @ManyToOne
    private GoodsComment goodsComment;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public GoodsCommentLikes(GoodsComment goodsComment, User user){
        this.goodsComment = goodsComment;
        this.user = user;
    }
}
