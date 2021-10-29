package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginCheckResponseDto {
    private String username;
    private String myteam;
    private List<Long> myTimeLineLikesList;
    private List<Long> myGoodsLikesList;
}
