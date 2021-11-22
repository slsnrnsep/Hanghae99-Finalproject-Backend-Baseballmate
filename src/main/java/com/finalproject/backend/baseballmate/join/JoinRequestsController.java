package com.finalproject.backend.baseballmate.join;

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

}
