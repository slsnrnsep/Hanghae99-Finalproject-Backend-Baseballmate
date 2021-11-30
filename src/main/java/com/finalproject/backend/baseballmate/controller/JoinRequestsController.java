package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.service.JoinRequestsService;
import com.finalproject.backend.baseballmate.responseDto.MyAwaitRequestJoinResponseDto;
import com.finalproject.backend.baseballmate.responseDto.UserInfoAndPostResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"8. 참여관련 승인/미승인 + 알람"}) // Swagger
public class JoinRequestsController {

    private final JoinRequestsService joinRequestsService;

    @ApiOperation(value = "모임 참여 신청", notes = "모임 참여 신청")
    @GetMapping("/groups/join/request/{postId}")
    public String requestGroupJoin(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return joinRequestsService.requestGroupJoin(userDetails, postId);
    }

    @ApiOperation(value = "모임 참여 신청 취소", notes = "모임 참여 신청 취소")
    @DeleteMapping("/groups/join/request/{id}")
    public String requestGroupJoinCancel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return joinRequestsService.requestGroupJoinCancel(userDetails, id);
    }

    @ApiOperation(value = "모임 참여 신청 목록", notes = "모임 참여 신청 목록")
    @GetMapping("/groups/join/request/list")
    public List<UserInfoAndPostResponseDto> requestGroupJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.requestGroupJoinList(userDetails);
    }

    @ApiOperation(value = "모임 참여 신청 승인/비승인", notes = "모임 참여 신청 승인/비승인")
    @GetMapping("/groups/join/request/accept/{joinRequestId}")
    public String acceptJoinGroupRequest(@PathVariable Long joinRequestId, @RequestParam(value = "accept") boolean tOrF) {
        return joinRequestsService.acceptJoinGroupRequest(joinRequestId, tOrF);
    }

    @ApiOperation(value = "나의 모임 참여 신청 대기 목록", notes = "나의 모임 참여 신청 대기 목록")
    @GetMapping("/groups/join/request/await")
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestGroupJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.myAwaitRequestGroupJoinList(userDetails);
    }

    @ApiOperation(value = "스크린 참여 신청", notes = "스크린 참여 신청")
    @GetMapping("/screen/join/request/{postId}")
    public String requestScreenJoin(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return joinRequestsService.requestScreenJoin(userDetails, postId);
    }

    @ApiOperation(value = "스크린 참여 신청 취소", notes = "스크린 참여 신청 취소")
    @DeleteMapping("/screen/join/request/{id}")
    public String requestScreenJoinCancel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return joinRequestsService.requestScreenJoinCancel(userDetails, id);
    }

    @ApiOperation(value = "스크린 참여 신청 목록", notes = "스크린 참여 신청 목록")
    @GetMapping("/screen/join/request/list")
    public List<UserInfoAndPostResponseDto> requestScreenJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.requestScreenJoinList(userDetails);
    }

    @ApiOperation(value = "스크린 참여 신청 승인/비승인", notes = "스크린 참여 신청 승인/비승인")
    @GetMapping("/screen/join/request/accept/{joinRequestId}")
    public String acceptScreenJoinRequest(@PathVariable Long joinRequestId, @RequestParam(value = "accept") boolean tOrF) {
        return joinRequestsService.acceptScreenJoinRequest(joinRequestId, tOrF);
    }

    @ApiOperation(value = "나의 스크린 참여 신청 대기 목록", notes = "나의 스크린 참여 신청 대기 목록")
    @GetMapping("/screen/join/request/await")
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestScreenJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.myAwaitRequestScreenJoinList(userDetails);
    }

}
