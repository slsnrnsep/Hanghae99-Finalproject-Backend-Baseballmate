package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.MatchRepository;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.service.GoodsService;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.MatchDataService;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
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
    private final MatchRepository matchRepository;
    private final MatchDataService matchDataService;
    //내가 응원하는 구단의 가장 최근 경기 일정 조회
    //@PathVariable("myteam") String myteam, @AuthenticationPrincipal UserDetailsImpl userDetails
    @GetMapping("/main/myteamSchedule/{myteam}")
    public List<MatchInfomation> getmyteamSchedule(@PathVariable("myteam") String myteam)
    {
        return matchDataService.myteamselect(myteam);
    }

    //관리자만 사용가능해야합니다 (서버관리자가 지정해서 스케쥴러 돌릴예정)
    @GetMapping("/make/kbodata")
    public String  createkbodata() throws IOException {
        matchDataService.createKBODatas();
        return "서버DB에 KBODATA가 저장되었습니다.";
    }

    @GetMapping("/api/kbodata")
    public List<MatchInfomation> kbodata(){
        List<MatchInfomation> matchInfomationList = matchDataService.getKBODatas();
//        matchDataService.updateKBODatas(matchInfomationList);
        return matchInfomationList;
    }

    @GetMapping("/update/kbodata")
    public String updatekbodata() throws IOException {
        List<MatchInfomation> matchInfomationList = matchDataService.crawlingKBODatas();
        matchDataService.updateKBODatas(matchInfomationList);
        return "업데이트에 성공하였습니다. 데이터를 다시 조회해보세요";
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
