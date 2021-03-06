package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.TimeLine;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllTimeLineResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
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

            Long id = timeLine.getId();
            String userName = timeLine.getUserName();
            String content = timeLine.getContent();
            String dayBefore = getDayBefore(timeLine);
            int likeCount = timeLine.getLikeCount();

            AllTimeLineResponseDto responseDto =
                    new AllTimeLineResponseDto(id,userName, content, dayBefore, likeCount);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public List<AllTimeLineResponseDto> getnowTimeLine(int number) throws ParseException {
        List<TimeLine> timeLineList = timeLineRepository.findAllByOrderByCreatedAtDesc();

        List<AllTimeLineResponseDto> data = new ArrayList<>();

        if(timeLineList.size()<=number) {
            number = timeLineList.size();
        }

        for(int i=0; i<number; i++) {
            TimeLine timeLine = timeLineList.get(i);

            Long id = timeLine.getId();
            String userName = timeLine.getUserName();
            String content = timeLine.getContent();
            String dayBefore = getDayBefore(timeLine);
            int likeCount = timeLine.getLikeCount();

            AllTimeLineResponseDto responseDto =
                    new AllTimeLineResponseDto(id, userName, content, dayBefore, likeCount);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public MsgResponseDto createTimeLine(UserDetailsImpl userDetails, TimeLineRequestDto requestDto) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ??? ???????????? ?????? ???????????????");
        }
        User loginedUser = userDetails.getUser();
        TimeLine timeLine = new TimeLine(loginedUser,requestDto);
        timeLineRepository.save(timeLine);
        MsgResponseDto timeLineResponseDto = new MsgResponseDto("success","?????? ??????");
        return timeLineResponseDto;
    }

    public String getDayBefore(TimeLine timeLine) throws ParseException {

        //LocalDateTime -> Date?????? ??????
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = timeLine.getCreatedAt();

        Date format1 = java.sql.Timestamp.valueOf(now);
        Date format2 = java.sql.Timestamp.valueOf(createdAt);


        // date.getTime() : Date??? ?????????????????? ??????. 1??? = 1000?????????
        Long diffSec = (format1.getTime() - format2.getTime()) / 1000; // ??? ??????
        Long diffMin = (format1.getTime() - format2.getTime()) / 60000; // ??? ??????
        Long diffHour = (format1.getTime() - format2.getTime()) / 3600000; // ??? ??????, 24????????? ??????
        Long diffDays = diffSec / (24 * 60 * 60); // ????????? ?????? ???:7???, 6???

        //DayBefore ??????
        // ??? ????????? 60??? ????????? ??? -> return ??? ??????
        // ??? ????????? 60??? ??????, ??? ????????? 60??? ????????? ??? -> return ??? ??????
        // ??? ????????? 60??? ??????, ??? ????????? 24 ????????? ??? -> return ??? ??????
        // ??? ????????? 24 ??????, ??? ????????? 7??? ????????? ??? -> return ????????? ??????
        // ??? ????????? 7??? ????????? ??? -> return createdAt??? ???, ???, ?????????

        String dayBefore = "";

        if(diffSec < 60) {
            String secstr = diffSec.toString();
            dayBefore = secstr + "??? ???";
        } else if(diffSec >= 60 && diffMin < 60) {
            String minstr = diffMin.toString();
            dayBefore = minstr + "??? ???";
        } else if(diffMin >= 60 && diffHour < 24) {
            String hourstr = diffHour.toString();
            dayBefore = hourstr + "?????? ???";
        } else if(diffHour >= 24 && diffDays < 7) {
            String daystr = diffDays.toString();
            dayBefore = daystr + "??? ???";
        } else if (diffDays > 7) {
            dayBefore = format2.toString();
        }
        return dayBefore;
    }

    @Transactional
    public MsgResponseDto deletetimeLine(Long id, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("????????? ??? ???????????? ?????? ???????????????");
        }

        String loginedUsername = userDetails.getUser().getUserid();
        String commentUsername = "";

        TimeLine timeLine = timeLineRepository.findById(id).orElseThrow(
                ()->new IllegalArgumentException("??????????????? ?????? ??? ????????????.")
        );
        if(timeLine!=null)
        {

            commentUsername = timeLine.getCreatedUser().getUserid();

            if(!loginedUsername.equals(commentUsername)) {
                throw new IllegalArgumentException("?????? ????????? ????????????.");
            }
            timeLineRepository.deleteById(id);
            MsgResponseDto timeLineResponseDto = new MsgResponseDto("success", "?????? ??????");
            return timeLineResponseDto;

        }
        else {
            throw new NullPointerException("?????? ????????? ???????????? ????????????.");
        }

    }
}
