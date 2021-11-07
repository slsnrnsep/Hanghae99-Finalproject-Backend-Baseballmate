package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeLineController {

    private final TimeLineRepository timeLineRepository;
    private final TimeLineService timeLineService;

    @GetMapping("/timelines")
    public List<AllTimeLineResponseDto> getTimeLine() throws ParseException
    {
        List<AllTimeLineResponseDto> allTimeLine = timeLineService.getTimeLine();
        return allTimeLine;
    }

    @PostMapping("/timelines")
    public MsgResponseDto postTimeLine(
            @RequestBody TimeLineRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }
        try
        {
            timeLineService.createTimeLine(userDetails.getUser(), requestDto);
            MsgResponseDto timeLineResponseDto = new MsgResponseDto("success","작성 완료");
            return timeLineResponseDto;
        }
        catch (Exception e)
        {
            MsgResponseDto timeLineResponseDto = new MsgResponseDto(e.toString(),"에러가 발생하였습니다.");
            return timeLineResponseDto;
        }

    }



    @DeleteMapping("/timelines/{timeLineId}")
    public MsgResponseDto deleteTimeLine(
            @PathVariable("timeLineId") Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }

        timeLineService.deletetimeLine(id, userDetails);
        MsgResponseDto timeLineResponseDto = new MsgResponseDto("success", "삭제 완료");
        return timeLineResponseDto;

    }

}
