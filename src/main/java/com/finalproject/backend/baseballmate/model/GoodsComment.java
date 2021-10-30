package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.GoodsCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goodsId")
    private Goods goods;

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
