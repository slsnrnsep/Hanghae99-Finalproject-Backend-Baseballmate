package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Goods extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goods_id")
    private Long id;

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

    @OneToMany(mappedBy = "goods")
    private List<GoodsLikes> likesList;

    @Column(columnDefinition = "integer default 0")
    private int likeCount;

    // 굿즈 등록 생성자
    public Goods(String userName, GoodsRequestDto requestDto){
        this.userName = userName;
        this.goodsName = requestDto.getGoodsName();
        this.goodsPrice = requestDto.getGoodsPrice();
        this.goodsContent = requestDto.getGoodsContent();
        this.goodsImg = requestDto.getGoodsImg();
    }

    // 굿즈 업데이트 생성자
    public void update(GoodsRequestDto requestDto) {
        this.goodsName = requestDto.getGoodsName();
        this.goodsPrice = requestDto.getGoodsPrice();
        this.goodsContent = requestDto.getGoodsContent();
        this.goodsImg = requestDto.getGoodsImg();
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
