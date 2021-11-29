package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.GoodsComment;
import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsCommentRepository;
import com.finalproject.backend.baseballmate.repository.GoodsLikesRepository;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsDetailRequestDto;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsDetailResponseDto;
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
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final GoodsCommentRepository goodsCommentRepository;
    private final GoodsLikesRepository goodsLikesRepository;
    private final UserRepository userRepository;

    @Transactional
    public Goods createGoods(User loginUser, GoodsRequestDto requestDto) {
        String goodsUserPicture = loginUser.getPicture();
        String myTeam = loginUser.getMyselectTeam();
        String userAddress = loginUser.getAddress();

        Long userId = loginUser.getId();
        String usertype = "";
        if(loginUser.getKakaoId() == null)
        {
            usertype = "normal";
        }
        else
        {
            usertype = "kakao";
        }
        Goods goods = new Goods(loginUser, requestDto, goodsUserPicture, myTeam, userAddress, userId,usertype);
        goodsRepository.save(goods);
        return goods;
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
            String goodsContent = goods.getGoodsContent();
            String filePath = goods.getFilePath();
            String dayBefore = getDayBefore(goods);
            String goodsUserPicture = goods.getGoodsUserPicture();
//            int likeCount = goods.getLikeCount();
            List<GoodsComment> goodsCommentList = goodsCommentRepository.findAllByGoods_IdOrderByCreatedAtAsc(id);
            List<GoodsLikes> goodsLikesList = goodsLikesRepository.findAllByGoods_Id(goods.getId());
            String myTeam = goods.getMyTeam();
            String userAddress = goods.getUserAddress();

            Long userId = goods.getUserId();
            String usertype = goods.getUsertype();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id,userName ,goodsName,goodsContent,filePath,dayBefore, goodsUserPicture, goodsCommentList,goodsLikesList, myTeam, userAddress, userId,usertype);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public List<AllGoodsResponseDto> getnowGoods(int number, UserDetailsImpl userDetails) throws ParseException {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다")
        );
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
            String goodsContent = goods.getGoodsContent();
            String filePath = goods.getFilePath();
            String dayBefore = getDayBefore(goods);
            String goodsUserPicture = goods.getGoodsUserPicture();
//            int likeCount = goods.getLikeCount();
            List<GoodsComment> goodsCommentList = goodsCommentRepository.findAllByGoods_IdOrderByCreatedAtAsc(id);
            List<GoodsLikes> goodsLikesList = goodsLikesRepository.findAllByGoods_IdAndUserId(goods.getId(), user.getId());
            String myTeam = goods.getMyTeam();
            String userAddress = goods.getUserAddress();
            Long userId = goods.getUserId();
            String usertype = goods.getUsertype();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id, userName, goodsName,goodsContent,filePath,dayBefore, goodsUserPicture, goodsCommentList,goodsLikesList, myTeam, userAddress,userId,usertype);
            data.add(responseDto);
        }
        return data;
    }

    public GoodsDetailResponseDto getGoodsDetail(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 아이디입니다.")
        );
        List<GoodsDetailRequestDto> goodsCommentList = new ArrayList<>();
        GoodsDetailRequestDto goodsDetailRequestDto = new GoodsDetailRequestDto();
        String userName = goods.getUserName();
        String goodsName = goods.getGoodsName();
        String goodsContent = goods.getGoodsContent();
        String filePath = goods.getFilePath();
        List<GoodsComment> goodsComments = goodsCommentRepository.findAllByGoodsId(goodsId);
        for (int i = 0; i < goodsComments.size(); i++ ) {
            goodsDetailRequestDto.setId(goodsComments.get(i).getId());
            goodsDetailRequestDto.setUserName(goodsComments.get(i).getUserName());
            goodsDetailRequestDto.setComment(goodsComments.get(i).getComment());
            goodsCommentList.add(goodsDetailRequestDto);
        }
        GoodsDetailResponseDto goodsDetailResponseDto =
                new GoodsDetailResponseDto(userName, goodsName, goodsContent, filePath,goodsCommentList);
        return goodsDetailResponseDto;
    }

    @Transactional
    public void updateGoods(Long id, GoodsRequestDto requestDto,UserDetailsImpl userDetails) {
        Goods goods = goodsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 아이디입니다.")
        );
        String loginUser = userDetails.getUser().getUserid();
        String writer = "";

        if(goods != null){
            writer = goods.getCreatedUser().getUserid();
            if(!loginUser.equals(writer)){
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
            goods.update(requestDto);
            goodsRepository.save(goods);
        }else{
            throw new NullPointerException("해당 굿즈가 존재하지 않습니다");
        }

    }

    public void deleteGoods(Long id, UserDetailsImpl userDetails) {
        String loginUser = userDetails.getUser().getUserid();
        String writer = "";

        Goods goods = goodsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다")
        );
        if(goods != null){
            writer = goods.getCreatedUser().getUserid();

            if(!loginUser.equals(writer)){
                throw new IllegalArgumentException("삭제권한이 없습니다");
            }
            goodsRepository.deleteById(id);
        }
        else{
            throw new NullPointerException("해당 굿즈가 존재하지 않습니다");
        }
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
