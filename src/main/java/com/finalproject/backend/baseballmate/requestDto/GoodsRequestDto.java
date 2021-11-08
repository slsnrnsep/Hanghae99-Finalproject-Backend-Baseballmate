package com.finalproject.backend.baseballmate.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsRequestDto {
    private String goodsName;
    private int goodsPrice;
    private String goodsContent;
    private String goodsImg;
}
