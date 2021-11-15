package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllGoodsResponseDto {
    private Long goodsId;
    private String userName;
    private String goodsName;
    private String goodsImg;
    private String dayBefore;
    private String goodsContent;
//  private int likecount;
//  private int commentcount;

}
