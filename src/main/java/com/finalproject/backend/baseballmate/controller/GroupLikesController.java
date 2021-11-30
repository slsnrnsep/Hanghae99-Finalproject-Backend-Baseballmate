package com.finalproject.backend.baseballmate.controller;


import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupLikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"2. 모임"}) // Swagger
public class GroupLikesController {

    private final GroupLikesService groupLikesService;

    //모임 좋아요 기능
    @ApiOperation(value = "모임 좋아요/취소", notes = "모임을 좋아요하거나 취소합니다.")
    @PostMapping("/groups/{groupId}/like")
    public String GroupLikePost(@PathVariable("groupId") Long groupId, @RequestBody LikesRequestDto likesRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupLikesService.groupLikes(groupId, likesRequestDto, userDetails);
    }
}
