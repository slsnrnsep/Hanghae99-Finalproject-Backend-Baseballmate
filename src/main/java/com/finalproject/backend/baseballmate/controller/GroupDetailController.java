package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GroupDetailController {

//    @PostMapping("/page/group/detail/apply/{groupId}")
//    public MsgResponseDto applyGroup(@PathVariable("groupId")Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        String loginedUserid = userDetails.getUser().getUserid();
//        if (loginedUserid == null) {
//            throw new IllegalArgumentException("로그인 한 사용자만 모임을 조회할 수 있습니다.");
//        }
//
//    }
}
