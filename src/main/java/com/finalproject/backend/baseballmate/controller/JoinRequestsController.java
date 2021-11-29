package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.service.JoinRequestsService;
import com.finalproject.backend.baseballmate.responseDto.MyAwaitRequestJoinResponseDto;
import com.finalproject.backend.baseballmate.responseDto.UserInfoAndPostResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JoinRequestsController {

    private final JoinRequestsService joinRequestsService;

    @ApiOperation(value = "게시글 입장 신청", notes = "게시글 입장 신청")
    @GetMapping("/groups/join/request/{postId}")
    public String requestJoin(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return joinRequestsService.requestJoin(userDetails, postId);
    }

    @ApiOperation(value = "게시글 입장 신청 취소", notes = "게시글 입장 신청 취소")
    @DeleteMapping("/groups/join/request/{id}")
    public String requestJoinCancel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return joinRequestsService.requestJoinCancel(userDetails, id);
    }

    @ApiOperation(value = "게시글 입장 신청 목록", notes = "게시글 입장 신청 목록")
    @GetMapping("/groups/join/request/list")
    public List<UserInfoAndPostResponseDto> requestJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.requestJoinList(userDetails);
    }

    @ApiOperation(value = "게시글 입장 신청 승인/비승인", notes = "게시글 입장 신청 승인/비승인")
    @GetMapping("/groups/join/request/accept/{joinRequestId}")
    public String acceptJoinRequest(@PathVariable Long joinRequestId, @RequestParam(value = "accept") boolean tOrF) {
        return joinRequestsService.acceptJoinRequest(joinRequestId, tOrF);
    }

    @ApiOperation(value = "나의 게시글 입장 신청 대기 목록", notes = "나의 게시글 입장 신청 대기 목록")
    @GetMapping("/groups/join/request/await")
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.myAwaitRequestJoinList(userDetails);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "스크린 입장 신청", notes = "스크린 입장 신청")
    @GetMapping("/screen/join/request/{postId}")
    public String requestJoin2(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return joinRequestsService.requestJoin2(userDetails, postId);
    }

    @ApiOperation(value = "스크린 입장 신청 취소", notes = "스크린 입장 신청 취소")
    @DeleteMapping("/screen/join/request/{id}")
    public String requestJoinCancel2(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return joinRequestsService.requestJoinCancel2(userDetails, id);
    }

    @ApiOperation(value = "스크린 입장 신청 목록", notes = "스크린 입장 신청 목록")
    @GetMapping("/screen/join/request/list")
    public List<UserInfoAndPostResponseDto> requestJoinList2(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.requestJoinList2(userDetails);
    }

    @ApiOperation(value = "스크린 입장 신청 승인/비승인", notes = "스크린 입장 신청 승인/비승인")
    @GetMapping("/screen/join/request/accept/{joinRequestId}")
    public String acceptJoinRequest2(@PathVariable Long joinRequestId, @RequestParam(value = "accept") boolean tOrF) {
        return joinRequestsService.acceptJoinRequest2(joinRequestId, tOrF);
    }

    @ApiOperation(value = "나의 스크린 입장 신청 대기 목록", notes = "나의 스크린 입장 신청 대기 목록")
    @GetMapping("/screen/join/request/await")
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinList2(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return joinRequestsService.myAwaitRequestJoinList2(userDetails);
    }

}
