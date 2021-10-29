package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.GoodsComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoodsDetailResponseDto {
    private String userName;
    private String goodsName;
    private int goodsPrice;
    private String goodsContent;
    private String goodsImg;
    List<String> goodsCommentList;
}
