package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.TimeLine;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.service.GoodsService;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final TimeLineRepository timeLineRepository;
    private final TimeLineService timeLineService;
    private final GoodsRepository goodsRepository;
    private final GoodsService goodsService;

    //내가 응원하는 구단의 가장 최근 경기 일정 조회
    @GetMapping("/main/myteamSchedule")
    public void getmyteamSchedule()
    {

    }
    //지금 핫한 모임 목록 조회(내가 응원하는 구단의 모임 + 잔여인원 적은 순으로 정렬)
    @GetMapping("/main/hotGroup")
    public void gethotGroup()
    {

    }
    //타임라인 조회(최신 등록 순)
    @GetMapping("/main/nowTimeline/{number}")
    public List<AllTimeLineResponseDto> getnowTimeLine(@PathVariable("number") int number) throws ParseException {
        List<AllTimeLineResponseDto> nowTimeLine = timeLineService.getnowTimeLine(number);
        return nowTimeLine;
    }
    //굿즈 조회(최신 등록 순)
    @GetMapping("/main/nowGoods/{number}")
    public List<AllGoodsResponseDto> getnowGoods(@PathVariable("number") int number) throws ParseException
    {
        List<AllGoodsResponseDto> nowGoods = goodsService.getnowGoods(number);
        return nowGoods;
    }

}
