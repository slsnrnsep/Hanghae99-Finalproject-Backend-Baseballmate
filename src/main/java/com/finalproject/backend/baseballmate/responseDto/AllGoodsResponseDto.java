package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.GoodsComment;
import com.finalproject.backend.baseballmate.model.GoodsLikes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllGoodsResponseDto {
    private Long goodsId;
    private String userName;
    private String goodsName;
    private String goodsContent;
    private String filePath;
    private String dayBefore;
//  private int likecount;
//  private int commentcount;
    private String goodsUserPicture;
    List<GoodsComment> goodsCommentList;
    List<GoodsLikes> goodsLikesList;
    String myTeam;
    String userAddress;
    Long userId;
    String usertype;
}
