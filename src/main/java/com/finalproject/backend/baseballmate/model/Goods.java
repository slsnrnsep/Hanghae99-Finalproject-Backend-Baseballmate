package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Goods {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsId;

    @Column
    private String userName; // 굿즈 게시글 작성자의 닉네임

    @Column
    private String goodsName;

    @Column
    private int goodsPrice;

    @Column
    private String goodsContent;

    @Column
    private String goodsImg;

    // 굿즈 등록 생성자
    public Goods(String userName, GoodsRequestDto requestDto){
        this.userName = userName;
        this.goodsName = requestDto.getGoodsName();
        this.goodsPrice = requestDto.getGoodsPrice();
        this.goodsContent = requestDto.getGoodsContent();
        this.goodsImg = requestDto.getGoodsImg();
    }

}
