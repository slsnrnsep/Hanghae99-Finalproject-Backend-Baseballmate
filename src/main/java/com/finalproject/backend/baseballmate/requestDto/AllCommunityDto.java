package com.finalproject.backend.baseballmate.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllCommunityDto {
    private Long communityId;
    private String userName;
    private String title;
    private String content;
    private String communityUserPicture;
    private String filePath;
    private String myTeam;
}
