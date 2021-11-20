package com.finalproject.backend.baseballmate.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommunityDetailResponseDto {
    private String userName;
    private String content;
    private String communityUserPicture;
    private String filePath;
    private String myTeam;
}
