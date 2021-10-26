package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;

    // 모임페이지 생성된 전체 조회
    @GetMapping("/page/group")
    public AllGroupResponseDto getAllGroups() {

    }

    // 모임 생성
    @PostMapping("/page/group")
    public MsgResponseDto createGroup() {

    }
}
