package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllGoodsResponseDto {
    private String userName;
    private String goodsName;
    private int goodsPrice;
    private String goodsImg;
    private String dayBefore;
//  private int likecount;
//  private int commentcount;

}
