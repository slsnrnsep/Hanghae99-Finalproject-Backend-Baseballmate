package com.finalproject.backend.baseballmate.requestDto;

import com.finalproject.backend.baseballmate.model.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllCommunityDto {
    private Long communityId;
    private String userName;
    private String content;
    private String communityUserPicture;
    private String filePath;
    private String myTeam;
}
