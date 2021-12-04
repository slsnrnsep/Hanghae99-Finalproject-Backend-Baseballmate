package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.AddressEnum;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.ScreenService;
import com.finalproject.backend.baseballmate.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"9-1. 마이페이지"}) // Swagger
public class MypageController {

    private final GroupService groupService;
    private final UserService userService;
    private final ScreenService screenService;


    //내가 작성한 그룹 조회
    @GetMapping("/my/groups/write")
    @ApiOperation(value = "내가 작성한 모임 조회", notes = "내가 작성한 모임 조회")
    public List<AllGroupResponseDto> MyGroupsWrite(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMywriteAllGroups(userDetails.getUser());
    }

    //내가 참여한 그룹 조회
    @GetMapping("/my/groups/applications")
    @ApiOperation(value = "내가 참여한 모임 조회", notes = "내가 참여한 모임 조회")
    public List<AllGroupResponseDto> MyGroupsApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMyapplicationAllGroups(userDetails.getUser());
    }

    //내가 좋아요한 그룹 조회
    @GetMapping("/my/groups/like")
    @ApiOperation(value = "내가 좋아요(찜)한 모임 조회", notes = "내가 좋아요(찜)한 모임 조회")
    public List<AllGroupResponseDto> MyGroupsLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }

        return groupService.getMylikeAllGroups(userDetails.getUser());
    }

    // 내가 좋아요한 스크린 모임 조회
    @GetMapping("/my/screen/like")
    @ApiOperation(value = "내가 좋아요(찜)한 스크린모임 조회", notes = "내가 좋아요(찜)한 스크린모임 조회")
    public List<AllScreenResponseDto> MyScreensLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }

        return screenService.getMylikeAllScreens(userDetails.getUser());
    }


    // 내가 작성한 스크린 모임 조회
    @GetMapping("/my/screen/write")
    @ApiOperation(value = "내가 작성한 스크린모임 조회", notes = "내가 작성한 스크린모임 조회")
    public List<AllScreenResponseDto> MyScreenWrite(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return screenService.getMywriteAllScreens(userDetails.getUser());
    }

    // 내가 참여한 스크린모임 조회
    @GetMapping("/my/screen/applications")
    @ApiOperation(value = "내가 참여한 스크린모임 조회", notes = "내가 참여한 스크린모임 조회")
    public List<AllScreenResponseDto> MyScreensApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return screenService.getMyapplicationAllScreens(userDetails.getUser());
    }


    //프론트 요청에 의해 급하게 만듬
    @GetMapping("/my/allmeeting")
    @ApiOperation(value = "나와 관련된 모든 데이터 조회", notes = "나와 관련된 모든 데이터 조회")
    public List MyAllMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        List allMeetingList = new ArrayList();
        allMeetingList.add(groupService.getMywriteAllGroups(userDetails.getUser()));
        allMeetingList.add(groupService.getMyapplicationAllGroups(userDetails.getUser()));
        allMeetingList.add(groupService.getMylikeAllGroups(userDetails.getUser()));
        allMeetingList.add(screenService.getMywriteAllScreens(userDetails.getUser()));
        allMeetingList.add(screenService.getMyapplicationAllScreens(userDetails.getUser()));
        allMeetingList.add(screenService.getMylikeAllScreens(userDetails.getUser()));

        return allMeetingList;
    }

    // 지역 정보 조회
    @GetMapping("/my/address")
    @ApiOperation(value = "나의 지역선택 데이터 조회", notes = "나의 지역선택 데이터 조회")
    public List<AddressEnum> getAddressEnumList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getAddressEnumList(userDetails);
    }

}
