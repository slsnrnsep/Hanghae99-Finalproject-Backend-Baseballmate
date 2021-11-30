package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"1. 타임라인"}) // Swagger
public class TimeLineController {

    private final TimeLineService timeLineService;

    // 타임라인 생성
    @ApiOperation(value = "타임라인 작성", notes = "타임라인을 작성합니다.")
    @PostMapping("/timelines")
    public MsgResponseDto postTimeLine(@RequestBody TimeLineRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return timeLineService.createTimeLine(userDetails, requestDto);
    }

    // 타임라인 조회
    @ApiOperation(value = "타임라인 조회", notes = "타임라인을 조회합니다.")
    @GetMapping("/timelines")
    public List<AllTimeLineResponseDto> getTimeLine() throws ParseException
    {
        return timeLineService.getTimeLine();
    }

    // 타임라인 삭제
    @ApiOperation(value = "타임라인 삭제", notes = "타임라인을 삭제합니다.")
    @DeleteMapping("/timelines/{timeLineId}")
    public MsgResponseDto deleteTimeLine(@PathVariable("timeLineId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return timeLineService.deletetimeLine(id, userDetails);
    }

}
