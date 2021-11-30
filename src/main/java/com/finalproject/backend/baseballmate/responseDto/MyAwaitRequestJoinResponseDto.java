package com.finalproject.backend.baseballmate.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyAwaitRequestJoinResponseDto {
    private final Long joinRequestId;
    private final String postTitle;
    private final String userName;
    private final String userId;
    private final Long postId;

    @Builder
    public MyAwaitRequestJoinResponseDto(Long joinRequestId, String postTitle,String userName,String userId,Long postId) {
        this.joinRequestId = joinRequestId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.userId = userId;
        this.postId = postId;
    }
}