package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.model.TeamEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchDataService {

    private static String KBO_URL = "https://sports.news.naver.com/kbaseball/schedule/index";
    String savedate = "";

    @PostConstruct
    public List<MatchInfomation> getKBODatas() throws IOException {
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
            if(!tdContents.select("span[class=td_none]").isEmpty()){
                //프로 야구 경기가 없을때는 포문 탈출
                break;
            }
            if (!tdContents.select("span[class=td_date]").isEmpty()) {
                int count = tdContents.size();
                String hometeam = tdContents.get(2).text().split(" ")[0];
                String awayteam = tdContents.get(2).text().split(" ")[2];

                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(tdContents.get(0).text())
                        .time(tdContents.get(1).text())
                        .match(tdContents.get(2).text())
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                savedate = tdContents.get(0).text();
                matchInfomationList.add(MatchStats);
            }
            else {
                int count = tdContents.size();
                String hometeam = tdContents.get(1).text().split(" ")[0];
                String awayteam = tdContents.get(1).text().split(" ")[2];
                MatchInfomation MatchStats = MatchInfomation.builder()
                        .date(savedate)
                        .time(tdContents.get(0).text())
                        .match(tdContents.get(1).text())
                        .location(tdContents.get(count - 2).text())
                        .homeImage(TeamEnum.valueOf(hometeam).getValue())
                        .awayImage(TeamEnum.valueOf(awayteam).getValue())
                        .build();
                matchInfomationList.add(MatchStats);
            }

        }
        return matchInfomationList;
    }
}
