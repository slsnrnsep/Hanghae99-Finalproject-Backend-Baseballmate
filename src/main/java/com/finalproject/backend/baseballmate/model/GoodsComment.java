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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goodsId")
    private Goods goods;

    public GoodsComment(String userName,GoodsCommentRequestDto requestDto, Goods goods){
        this.userName = userName;
        this.comment = requestDto.getComment();
        this.goods = goods;
    }

    public void updateComment(GoodsCommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
