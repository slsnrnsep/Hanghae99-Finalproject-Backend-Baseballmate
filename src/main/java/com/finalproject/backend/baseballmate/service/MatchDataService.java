package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.model.TeamEnum;
import com.finalproject.backend.baseballmate.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchDataService {

    private final MatchRepository matchRepository;

    private static String KBO_URL = "https://sports.news.naver.com/kbaseball/schedule/index";
    String savedate = "";

    @Transactional
    public void createKBODatas() throws IOException {
        List<MatchInfomation> matchInfomationList = new ArrayList<>();

        Document doc = Jsoup.connect(KBO_URL).get();
        Elements contents = doc.select("table tbody tr");
        //test
//        Elements contents2 = doc.select("span[class=td_date]");
//
//        System.out.println(contents2);

        for (Element content : contents) {
            Elements tdContents = content.select("td");
            //test
//            System.out.println(tdContents.get(1).select("img").first());
//            System.out.println("=======111111==========================");
//            System.out.println(tdContents.get(1).select("img").attr("src"));
//            System.out.println("===========222222======================");
            if (tdContents.select("a").text().contains("홈으로")) {
                //마지막에 도달했다면 끝내주는 함수
                break;
            }
            if (!tdContents.select("span[class=td_none]").isEmpty()) {
                //프로 야구 경기가 없을때는 포문 탈출
                continue;
            }
//            if(!tdContents.select("a"))
            if (!tdContents.select("span[class=td_date]").isEmpty()) {
                int count = tdContents.size();
                String hometeam = tdContents.get(2).text().split(" ")[0];
                String awayteam = tdContents.get(2).text().split(" ")[2];

                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(tdContents.get(0).text())
                        .time(tdContents.get(1).text())
                        .match(tdContents.get(2).text())
                        .hometeam(hometeam)
                        .awayteam(awayteam)
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                savedate = tdContents.get(0).text();
                matchInfomationList.add(MatchStats);
                matchRepository.save(MatchStats);
            } else {
                int count = tdContents.size();
                String hometeam = tdContents.get(1).text().split(" ")[0];
                String awayteam = tdContents.get(1).text().split(" ")[2];

                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(savedate)
                        .time(tdContents.get(0).text())
                        .match(tdContents.get(1).text())
                        .hometeam(hometeam)
                        .awayteam(awayteam)
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                matchInfomationList.add(MatchStats);
                matchRepository.save(MatchStats);
            }
        }
    }

    @Transactional
    public List<MatchInfomation> crawlingKBODatas() throws IOException {
        List<MatchInfomation> matchInfomationList = new ArrayList<>();

        Document doc = Jsoup.connect(KBO_URL).get();
        Elements contents = doc.select("table tbody tr");
        //test
//        Elements contents2 = doc.select("span[class=td_date]");
//
//        System.out.println(contents2);

        for (Element content : contents) {
            Elements tdContents = content.select("td");
            //test
//            System.out.println(tdContents.get(1).select("img").first());
//            System.out.println("=======111111==========================");
//            System.out.println(tdContents.get(1).select("img").attr("src"));
//            System.out.println("===========222222======================");
            if (tdContents.select("a").text().contains("홈으로")) {
                //마지막에 도달했다면 끝내주는 함수
                break;
            }
            if (!tdContents.select("span[class=td_none]").isEmpty()) {
                //프로 야구 경기가 없을때는 포문 탈출
                continue;
            }
//            if(!tdContents.select("a"))
            if (!tdContents.select("span[class=td_date]").isEmpty()) {
                int count = tdContents.size();
                String hometeam = tdContents.get(2).text().split(" ")[0];
                String awayteam = tdContents.get(2).text().split(" ")[2];

                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(tdContents.get(0).text())
                        .time(tdContents.get(1).text())
                        .match(tdContents.get(2).text())
                        .hometeam(hometeam)
                        .awayteam(awayteam)
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                savedate = tdContents.get(0).text();
                matchInfomationList.add(MatchStats);

            } else {
                int count = tdContents.size();
                String hometeam = tdContents.get(1).text().split(" ")[0];
                String awayteam = tdContents.get(1).text().split(" ")[2];

                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(savedate)
                        .time(tdContents.get(0).text())
                        .match(tdContents.get(1).text())
                        .hometeam(hometeam)
                        .awayteam(awayteam)
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                matchInfomationList.add(MatchStats);
            }
        }
        return matchInfomationList;
    }

    @Transactional
    public List<MatchInfomation> getKBODatas() {
        List<MatchInfomation> matchInfomationList = matchRepository.findAllByOrderByMatchIdAsc();

        return matchInfomationList;
    }

    @Transactional
    public void updateKBODatas(List<MatchInfomation> requestDto) {
        List<MatchInfomation> matchInfomation = matchRepository.findAllByOrderByMatchIdAsc();
        for (int i = 0; i < requestDto.size(); i++) {

            MatchInfomation updateinfomation = matchInfomation.get(i);

            updateinfomation.update(requestDto.get(i).getDate(), requestDto.get(i).getTime(),
                    requestDto.get(i).getMatch(), requestDto.get(i).getLocation(),
                    requestDto.get(i).getHomeImage(), requestDto.get(i).getAwayImage());


            matchRepository.save(updateinfomation);
        }

    }

    @Transactional
    public List<MatchInfomation> myteamselect(String myteam) {
        List<MatchInfomation> myTeamMatchList = new ArrayList<>();
//        List<MatchInfomation> myTeamawayMatchList = new ArrayList<>();

        List<MatchInfomation> matchInfomationList = getKBODatas();
        MatchInfomation myteamMatch;
        for (int i = 0; i < matchInfomationList.size(); i++) {

            //홈팀에 내 팀이 있거나 , 어웨이팀에 내팀이 있으면
            //KT
            // true -> kt
            switch (matchInfomationList.get(i).getMatch().split(" ")[0]) {
                case "NC":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "두산":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "KT":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "LG":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "키움":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "KIA":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "롯데":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "삼성":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "SSG":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "한화":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                default:
                    throw new IllegalArgumentException("내가 선택한 구단의 경기를 찾을 수 없습니다");
            }
            if (myteamMatch.getHometeam().equals(myteam)) {
                myTeamMatchList.add(myteamMatch);
            }

            //롯데
            switch (matchInfomationList.get(i).getMatch().split(" ")[2]) {
                case "NC":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "두산":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "KT":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "LG":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "키움":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "KIA":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "롯데":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "삼성":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "SSG":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                case "한화":
                    myteamMatch = matchInfomationList.get(i);
                    break;
                default:
                    throw new IllegalArgumentException("내가 선택한 구단의 경기를 찾을 수 없습니다");
            }

            if (myteamMatch.getAwayteam().equals(myteam)) {
                myTeamMatchList.add(myteamMatch);
            }

//            KT 들어간 홈팀,어웨이든
//             홈팀으로 KT드간 리스트 , 어웨이랑 KT드간 리스트

//
//        // 홈팀 어웨이 구분없이
//        MyteamMatchResponseDto myteamMatchResponseDto = new MyteamMatchResponseDto(myTeamhomeMatchList,myTeamawayMatchList);
//        return myteamMatchResponseDto;

        }
        return myTeamMatchList;
    }
}
