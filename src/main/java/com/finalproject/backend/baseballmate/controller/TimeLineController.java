package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.TimeLine;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import com.finalproject.backend.baseballmate.responseDto.TimeLineResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeLineController {

    private final TimeLineRepository timeLineRepository;
    private final TimeLineService timeLineService;

    @GetMapping("/page/timeLine")
    public List<TimeLine> getTimeLine()
    {
        List<TimeLine> allTimeLine = timeLineService.getTimeLine();
        return allTimeLine;
    }
    @PostMapping("/page/timeLine")
    public TimeLineResponseDto postTimeLine(@RequestBody TimeLineRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }
        try
        {
            timeLineService.createTimeLine(userDetails.getUser().getUsername(), requestDto);
            TimeLineResponseDto timeLineResponseDto = new TimeLineResponseDto("success","작성 완료");
            return timeLineResponseDto;
        }
        catch (Exception e)
        {
            TimeLineResponseDto timeLineResponseDto = new TimeLineResponseDto(e.toString(),"에러가 발생하였습니다.");
            return timeLineResponseDto;
        }

    }


    @DeleteMapping("/page/timeLine/{timeLineId}")
    public TimeLineResponseDto deleteTimeLine(@PathVariable("timeLineId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }
        try
        {
            timeLineRepository.deleteById(id);
            TimeLineResponseDto timeLineResponseDto = new TimeLineResponseDto("success","삭제 완료");
            return timeLineResponseDto;
        }
        catch (Exception e)
        {
            TimeLineResponseDto timeLineResponseDto = new TimeLineResponseDto(e.toString(),"에러가 발생하였습니다.");
            return timeLineResponseDto;
        }

    }

}
