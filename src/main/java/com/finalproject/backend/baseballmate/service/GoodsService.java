package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
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
public class GoodsService {
    private final GoodsRepository goodsRepository;

    @Transactional
    public void createGoods(String username, GoodsRequestDto requestDto) {
        Goods goods = new Goods(username, requestDto);
        goodsRepository.save(goods);
    }

    @Transactional
    public List<AllGoodsResponseDto> getGoods() throws ParseException {
        List<Goods> goodsList = goodsRepository.findAllByOrderByCreatedAtDesc();

        List<AllGoodsResponseDto> data = new ArrayList<>();
        for(int i=0; i<goodsList.size(); i++) {
            Goods goods = goodsList.get(i);

            Long id = goods.getId();
            String userName = goods.getUserName();
            String goodsName = goods.getGoodsName();
            int goodsPrice = goods.getGoodsPrice();
            String goodsImg = goods.getGoodsImg();
            String dayBefore = getDayBefore(goods);

//            int likeCount = goods.getLikeCount();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id,userName, goodsName,goodsPrice,goodsImg,dayBefore);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public List<AllGoodsResponseDto> getnowGoods(int number) throws ParseException {
        List<Goods> goodsList = goodsRepository.findAllByOrderByCreatedAtDesc();

        List<AllGoodsResponseDto> data = new ArrayList<>();

        if(goodsList.size()<=number) {
            number = goodsList.size();
        }

        for(int i=0; i<number; i++) {
            Goods goods = goodsList.get(i);

            Long id = goods.getId();
            String userName = goods.getUserName();
            String goodsName = goods.getGoodsName();
            int goodsPrice = goods.getGoodsPrice();
            String goodsImg = goods.getGoodsImg();
            String dayBefore = getDayBefore(goods);

//            int likeCount = goods.getLikeCount();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id, userName, goodsName,goodsPrice,goodsImg,dayBefore);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public void updateGoods(Long id, GoodsRequestDto requestDto) {
        Goods goods = goodsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 굿즈입니다.")
        );
        goods.update(requestDto);

        goodsRepository.save(goods);
    }

    public String getDayBefore(Goods goods) throws ParseException {

        //LocalDateTime -> Date으로 변환
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = goods.getCreatedAt();

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
