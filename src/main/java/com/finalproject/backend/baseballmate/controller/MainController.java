package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.responseDto.HotGroupResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"9. 메인"}) // Swagger
public class MainController {


    private final GroupService groupService;
    private final TimeLineService timeLineService;
    private final GoodsService goodsService;
    private final MatchDataService matchDataService;

    //관리자만 사용가능해야합니다(강제로 KBODATA를 DB에 쌓음)
    //현재 서버가 가동될 때마다 저장하도록 기능구현 되어있음
    //하지만 테스트 조건이기에 주석처리 해둠 @PostConstruct

    @GetMapping("/make/kbodata")
    @ApiOperation(value = "크롤링 데이터 생성", notes = "크롤링 데이터 생성")
    public String  createkbodata() throws IOException {
        matchDataService.createKBODatas();
        return "서버DB에 KBODATA가 저장되었습니다.";
    }

    @ApiOperation(value = "크롤링 데이터 조회", notes = "크롤링 데이터 조회")
    @GetMapping("/kbodatas")
    public List<MatchInfomation> kbodata(){
        return matchDataService.getKBODatas();
    }

    @GetMapping(path = "/kbodatas",params = "date")
    public List<MatchInfomation> kbodata(@RequestParam("date") String number){
        return matchDataService.getdateKBODatas(number);
    }

    //내가 응원하는 구단의 가장 최근 경기 일정 조회
    @GetMapping(path="/kbodatas",params = "team")
    @ApiOperation(value = "구단별 크롤링 데이터 조회", notes = "구단별 크롤링 데이터 조회")
    public List<MatchInfomation> getmyteamSchedule(@RequestParam("team") String myteam) throws UnsupportedEncodingException {
        URLDecoder.decode(myteam,"UTF-8");
        return matchDataService.myteamselect(myteam);
    }


//    @Scheduled(cron = "0 30 * * * *")
    //현재 야구 비시즌 관계로 주석처리 해두었습니다.
    @GetMapping("/update/kbodata")
    @ApiOperation(value = "크롤링 데이터 업데이트", notes = "크롤링 데이터 업데이트")
    public String updatekbodata() throws IOException {
        List<MatchInfomation> matchInfomationList = matchDataService.crawlingKBODatas();
        matchDataService.updateKBODatas(matchInfomationList);
//        System.out.println("업데이트가 실행되었습니다.매 시각 30분마다 한번");
        return "업데이트에 성공하였습니다. 데이터를 다시 조회해보세요";
    }


    //지금 핫한 모임 목록 조회(내가 응원하는 구단의 모임 + 잔여인원 적은 순으로 정렬)
    @ApiOperation(value = "핫한 모임 목록 조회", notes = "전체 구단의 모임 + 잔여인원 적은 순으로 정렬")
    @GetMapping("/groups/hotgroup")
    public List<HotGroupResponseDto> getHotGroupList() {
        return groupService.getHotGroups2();
    }

    //내가 응원하는 구단의 모임에서 핫한거만 출력
    @GetMapping(path="/groups/hotgroup",params = "team")
    @ApiOperation(value = "내가 응원하는 구단의 핫한 모임 목록 조회", notes = "내가 응원하는 구단의 모임 + 잔여인원 적은 순으로 정렬")
    public List<HotGroupResponseDto> getHotGroupList(@RequestParam("team") String myteam) throws UnsupportedEncodingException {
        URLDecoder.decode(myteam,"UTF-8");
        return groupService.getHotGroups(myteam);
    }

    //타임라인 조회(최신 등록 순)
    @GetMapping(path="/timelines",params = "count")
    @ApiOperation(value = "타임라인 조회(출력 개수 지정)", notes = "타임라인 조회(출력 개수 지정)")
    public List<AllTimeLineResponseDto> getnowTimeLine(@RequestParam("count") int number) throws ParseException {
        return timeLineService.getnowTimeLine(number);
    }
    //굿즈 조회(최신 등록 순)
    @GetMapping(path="/goods",params = "count")
    @ApiOperation(value = "굿즈 조회(출력 개수 지정)", notes = "굿즈 조회(출력 개수 지정)")
    public List<AllGoodsResponseDto> getnowGoods(@RequestParam("count") int number, @AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException
    {
        return goodsService.getnowGoods(number,userDetails);
    }

    @GetMapping("/images/{file}")
    @ApiOperation(value = "이미지 프론트에서 땡겨가는 API", notes = "이미지 프론트에서 땡겨가는 API")
    public ResponseEntity<Resource> display(
            @PathVariable("file") String file
    ) {

        String path = "/home/ubuntu/app/travis"+"/images/"+file; // 이경로는 우분투랑 윈도우랑 다르니까 주의해야댐 우분투 : / 윈도우 \\ 인것같음.
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
