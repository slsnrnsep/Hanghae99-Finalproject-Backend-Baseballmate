package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.ScreenCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(tags = {"3. 스크린모임"}) // Swagger
public class ScreenCommentController {

    private final ScreenCommentService screenCommentService;

    // 모임내 댓글 생성
    @ApiOperation(value = "스크린모임 댓글 작성", notes = "스크린모임의 댓글을 작성합니다.")
    @PostMapping("/screen/{screenId}/comment")
    public MsgResponseDto createScreenComment(@PathVariable("screenId") Long screenId, @RequestBody ScreenCommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenCommentService.createComment(commentRequestDto, screenId, userDetails);
    }

    // 모임 내 댓글 수정
    @ApiOperation(value = "스크린모임 댓글 수정", notes = "스크린모임의 댓글을 수정합니다.")
    @PutMapping("/screen/{screenId}/comment/{commentId}")
    public MsgResponseDto updateScreenComment(@PathVariable("screenId") Long screenId, @PathVariable("commentId") Long commentId, @RequestBody ScreenCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenCommentService.updateScreenComment(screenId, commentId, requestDto, userDetails);
    }

    // 모임 내 댓글 삭제
    @ApiOperation(value = "모임 댓글 삭제", notes = "모임의 댓글을 삭제합니다.")
    @DeleteMapping("/screen/{screenId}/comment/{commentId}")
    public MsgResponseDto deleteScreenComment(@PathVariable("screenId") Long screenId,@PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenCommentService.deleteScreenComment(commentId, userDetails);
    }
}
