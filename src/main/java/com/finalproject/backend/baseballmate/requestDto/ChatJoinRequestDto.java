package com.finalproject.backend.baseballmate.requestDto;

import lombok.Getter;

@Getter
public class ChatJoinRequestDto {
    private String username;
    private Long roomId;
    private String message;
}