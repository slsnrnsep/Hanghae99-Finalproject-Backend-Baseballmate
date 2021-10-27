package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.TimeLine;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeLineService {

    private final TimeLineRepository timeLineRepository;

    @Transactional
    public List<AllTimeLineResponseDto> getTimeLine() throws ParseException {
        List<TimeLine> timeLineList = timeLineRepository.findAllByOrderByCreatedAtDesc();

        List<AllTimeLineResponseDto> data = new ArrayList<>();

        for(int i=0; i<timeLineList.size(); i++) {
            TimeLine timeLine = timeLineList.get(i);


            String userName = timeLine.getUserName();
            String content = timeLine.getContent();
            String dayBefore = getDayBefore(timeLine);
            int likeCount = timeLine.getLikeCount();

            AllTimeLineResponseDto responseDto =
                    new AllTimeLineResponseDto(userName, content, dayBefore, likeCount);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public void createTimeLine(String userName,TimeLineRequestDto requestDto) {
        TimeLine timeLine = new TimeLine(userName,requestDto);
        timeLineRepository.save(timeLine);
    }

    public String getDayBefore(TimeLine timeLine) throws ParseException {

        //LocalDateTime -> Date으로 변환
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = timeLine.getCreatedAt();

        Date format1 = java.sql.Timestamp.valueOf(now);
        Date format2 = java.sql.Timestamp.valueOf(createdAt);


        // date.getTime() : Date를 밀리세컨드로 변환. 1초 = 1000밀리초
        Long diffSec = (format1.getTime() - format2.getTime()) / 1000; // 초 차이
        Long diffMin = (format1.getTime() - format2.getTime()) / 60000; // 분 차이
        Long diffHour = (format1.getTime() - format2.getTime()) / 3600000; // 시 차이, 24시까지 나옴
        Long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이 예:7일, 6일

        //DayBefore 계산
        // 초 차이가 60초 미만일 때 -> return 초 차이
        // 초 차이가 60초 이상, 분 차이가 60분 미만일 때 -> return 분 차이
        // 분 차이가 60분 이상, 시 차이가 24 미만일 때 -> return 시 차이
        // 시 차이가 24 이상, 일 차이가 7일 미만일 때 -> return 일자수 차이
        // 일 차이가 7일 이상일 때 -> return createdAt의 년, 월, 일까지

        String dayBefore = "";

        if(diffSec < 60) {
            String secstr = diffSec.toString();
            dayBefore = secstr + "초 전";
        } else if(diffSec >= 60 && diffMin < 60) {
            String minstr = diffMin.toString();
            dayBefore = minstr + "분 전";
        } else if(diffMin >= 60 && diffHour < 24) {
            String hourstr = diffHour.toString();
            dayBefore = hourstr + "시간 전";
        } else if(diffHour >= 24 && diffDays < 7) {
            String daystr = diffDays.toString();
            dayBefore = daystr + "일 전";
        } else if (diffDays > 7) {
            dayBefore = format2.toString();
        }
        return dayBefore;
    }
}
