package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.AddressEnum;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.ScreenService;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final UserService userService;

    private final ScreenRepository screenRepository;
    private final ScreenService screenService;


    //내가 작성한 그룹 조회
    @GetMapping("/my/groups/write")
    public List<AllGroupResponseDto> MyGroupsWrite(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMywriteAllGroups(userDetails.getUser());
    }

    //내가 참여한 그룹 조회
    @GetMapping("/my/groups/applications")
    public List<AllGroupResponseDto> MyGroupsApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMyapplicationAllGroups(userDetails.getUser());
    }

    //내가 좋아요한 그룹 조회
    @GetMapping("/my/groups/like")
    public List<AllGroupResponseDto> MyGroupsLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }

        return groupService.getMylikeAllGroups(userDetails.getUser());
    }

    // 내가 좋아요한 스크린 모임 조회
    @GetMapping("/my/screen/like")
    public List<AllScreenResponseDto> MyScreensLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }

        return screenService.getMylikeAllScreens(userDetails.getUser());
    }


    // 내가 작성한 스크린 모임 조회
    @GetMapping("/my/screen/write")
    public List<AllScreenResponseDto> MyScreenWrite(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return screenService.getMywriteAllScreens(userDetails.getUser());
    }

    // 내가 참여한 스크린모임 조회
    @GetMapping("/my/screen/applications")
    public List<AllScreenResponseDto> MyScreensApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return screenService.getMyapplicationAllScreens(userDetails.getUser());
    }


    @GetMapping("/my/allmeeting")
    public List MyAllMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        List allMeetingList = new ArrayList();
//        listToList(groupService.getMywriteAllGroups(userDetails.getUser()), allMeetingList);
//        listToList(groupService.getMyapplicationAllGroups(userDetails.getUser()), allMeetingList);
//        listToList(groupService.getMylikeAllGroups(userDetails.getUser()), allMeetingList);
//        listToList(screenService.getMywriteAllScreens(userDetails.getUser()), allMeetingList);
//        listToList(screenService.getMyapplicationAllScreens(userDetails.getUser()), allMeetingList);
//        listToList(screenService.getMylikeAllScreens(userDetails.getUser()), allMeetingList);
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
    public List<AddressEnum> getAddressEnumList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AddressEnum> addressEnumList = userService.getAddressEnumList(userDetails);
        return addressEnumList;
    }

    public static List listToList(List fromList, List toList) {

        for (int i = 0; i < fromList.size(); i++) {
            toList.add(fromList.get(i));
        }

        return toList;
    }
}
