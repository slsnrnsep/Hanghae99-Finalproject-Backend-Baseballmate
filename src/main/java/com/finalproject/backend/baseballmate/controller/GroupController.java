package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ResultResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;

    // 모임페이지 생성된 전체 조회
//    @GetMapping("/page/group")
//    public AllGroupResponseDto getAllGroups() {
//
//    }

    // 모임 생성
    @PostMapping("/page/group")
    public MsgResponseDto createGroup(@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String loginedUserid = userDetails.getUser().getUserid();
        if (loginedUserid == null) {
            throw new IllegalArgumentException("로그인 한 이용자만 모임을 생성할 수 있습니다.");
        }
        groupService.createGroup(requestDto, loginedUserid);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 등록 성공");

        return msgResponseDto;
    }
}
