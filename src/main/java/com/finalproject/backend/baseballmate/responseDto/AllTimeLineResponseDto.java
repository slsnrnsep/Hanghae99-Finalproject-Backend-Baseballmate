package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllTimeLineResponseDto
{
    private Long id;
    private String userName;
    private String content;
    private String dayBefore;
    private int likecount;
}
