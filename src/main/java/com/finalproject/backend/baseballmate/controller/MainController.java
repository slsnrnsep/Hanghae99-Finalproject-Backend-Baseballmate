package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.MatchRepository;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.responseDto.HotGroupResponseDto;
import com.finalproject.backend.baseballmate.service.GoodsService;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.MatchDataService;
import com.finalproject.backend.baseballmate.service.TimeLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    //관리자만 사용가능해야합니다(강제로 KBODATA를 DB에 쌓음)
    //현재 서버가 가동될 때마다 저장하도록 기능구현 되어있음
    //하지만 테스트 조건이기에 주석처리 해둠 @PostConstruct
    @GetMapping("/make/kbodata")
    public String  createkbodata() throws IOException {
        matchDataService.createKBODatas();
        return "서버DB에 KBODATA가 저장되었습니다.";
    }

    @GetMapping("/kbodatas")
    public List<MatchInfomation> kbodata(){
        List<MatchInfomation> matchInfomationList = matchDataService.getKBODatas();
//        matchDataService.updateKBODatas(matchInfomationList);
        return matchInfomationList;
    }

    //내가 응원하는 구단의 가장 최근 경기 일정 조회
    @GetMapping(path="/kbodatas",params = "team")
    public List<MatchInfomation> getmyteamSchedule(@RequestParam("team") String myteam)
    {
        return matchDataService.myteamselect(myteam);
    }

    @GetMapping("/update/kbodata")
    @Scheduled(cron = "0 30 * * * *")
    public String updatekbodata() throws IOException {
        List<MatchInfomation> matchInfomationList = matchDataService.crawlingKBODatas();
        matchDataService.updateKBODatas(matchInfomationList);
        System.out.println("업데이트가 실행되었습니다.매 시각 30분마다 한번");
        return "업데이트에 성공하였습니다. 데이터를 다시 조회해보세요";
    }

    //지금 핫한 모임 목록 조회(내가 응원하는 구단의 모임 + 잔여인원 적은 순으로 정렬)
    @GetMapping("/groups/hotgroup")
    public List<HotGroupResponseDto> getHotGroupList() {
        List<HotGroupResponseDto> hotGroupResponseDtoList = groupService.getHotGroups();
        return hotGroupResponseDtoList;
    }

    //타임라인 조회(최신 등록 순)
    @GetMapping(path="/timelines",params = "count")
    public List<AllTimeLineResponseDto> getnowTimeLine(@RequestParam("count") int number) throws ParseException {
        List<AllTimeLineResponseDto> nowTimeLine = timeLineService.getnowTimeLine(number);
        return nowTimeLine;
    }
    //굿즈 조회(최신 등록 순)
    @GetMapping(path="/goods",params = "count")
    public List<AllGoodsResponseDto> getnowGoods(@RequestParam("count") int number) throws ParseException
    {
        List<AllGoodsResponseDto> nowGoods = goodsService.getnowGoods(number);
        return nowGoods;
    }

    @GetMapping("/groups/{file}")
    public ResponseEntity<Resource> display(
            @PathVariable("file") String file
    ) {
        String path = System.getProperty("user.dir")+"\\images\\"+file; // 이경로는 우분투랑 윈도우랑 다르니까 주의해야댐 우분투 : / 윈도우 \\ 인것같음.
        String folder = "";
        org.springframework.core.io.Resource resource = new FileSystemResource(path);
        if (!resource.exists())
            return new ResponseEntity<org.springframework.core.io.Resource>(HttpStatus.NOT_FOUND);
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(path);
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            return null;
        }
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}
