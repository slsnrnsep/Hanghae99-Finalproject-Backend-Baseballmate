package com.finalproject.backend.baseballmate.config;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.*;
import com.finalproject.backend.baseballmate.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class DbSetConfig {
    private final UserService userService;
    private final GroupService groupService;
    private final TimeLineService timeLineService;
    private final GoodsService goodsService;
    private final MatchDataService matchDataService;
    private final GroupCommentService groupCommentService;
    private final GoodsCommentService goodsCommentService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GoodsRepository goodsRepository;
    public void dbset1() throws IOException {
        kboSet();
        userSet();
    }

    public void dbset2(){
        timelineSet();
//        groupSet();
        goodsSet();
    }

    public void dbset3(){
        commentSet();
    }

    @Transactional
    public void kboSet() throws IOException {
        matchDataService.createKBODatas();
    }

    @Transactional
    public void userSet()
    {
        UserRequestDto user = new UserRequestDto();
        user.setUserid("bbb@bbb.com");
        user.setUsername("bbb");
        user.setPassword("a123123!");
//        user.setProfileImage("/Profileimages/basic_profile");
        userService.registerUser(user);

        UserRequestDto user2 = new UserRequestDto();
        user2.setUserid("aaa@aaa.com");
        user2.setUsername("aaa");
        user2.setPassword("a123123!");
//        user2.setProfileImage("/Profileimages/basic_profile");
        userService.registerUser(user2);

        UserRequestDto user3 = new UserRequestDto();
        user3.setUserid("ccc@ccc.com");
        user3.setUsername("ccc");
        user3.setPassword("a123123!");
//        user2.setProfileImage("/Profileimages/basic_profile");
        userService.registerUser(user3);
    }

    @Transactional
    public void timelineSet()
    {
        TimeLineRequestDto reqdto = new TimeLineRequestDto();
        reqdto.setContent("임시테스트데이터1");
        timeLineService.createTimeLine("aaa",reqdto);

        reqdto.setContent("임시테스트데이터2");
        timeLineService.createTimeLine("bbb",reqdto);

        reqdto.setContent("임시테스트데이터3");
        timeLineService.createTimeLine("ccc",reqdto);

        reqdto.setContent("aaa작성,임시테스트");
        timeLineService.createTimeLine("aaa",reqdto);

        reqdto.setContent("bbb작성,임시테스트");
        timeLineService.createTimeLine("bbb",reqdto);

        reqdto.setContent("ccc작성,임시테스트");
        timeLineService.createTimeLine("ccc",reqdto);

        reqdto.setContent("aaa작성,좋아요테스트용");
        timeLineService.createTimeLine("aaa",reqdto);

        reqdto.setContent("bbb작성,수정,삭제 테스트용");
        timeLineService.createTimeLine("bbb",reqdto);
    }

//    @Transactional
//    public void groupSet()
//    {
//        GroupRequestDto requestDto = new GroupRequestDto();
//        requestDto.setTitle("aaa작성,그룹제목1");
//        requestDto.setContent("삼성응원가즈아ㅏㅏ");
//        requestDto.setGroupDate("11월7일");
//        requestDto.setPeopleLimit(3);
//        groupService.createGroup(requestDto,"aaa");
//
//        requestDto.setTitle("bbb작성,그룹제목1");
//        requestDto.setContent("롯데응원가실분");
//        requestDto.setGroupDate("11월25일");
//        requestDto.setPeopleLimit(5);
//        groupService.createGroup(requestDto,"bbb");
//
//        requestDto.setTitle("ccc작성,그룹제목1");
//        requestDto.setContent("ccccc11월25일 멍청이 ㄴㄴ");
//        requestDto.setGroupDate("11월25일");
//        requestDto.setPeopleLimit(9);
//        groupService.createGroup(requestDto,"ccc");
//
//        requestDto.setTitle("bbb작성,단둘이가자");
//        requestDto.setContent("선착순 1명");
//        requestDto.setGroupDate("12월25일");
//        requestDto.setPeopleLimit(2);
//        groupService.createGroup(requestDto,"ddd");
//    }
    @Transactional
    public void goodsSet(){
        GoodsRequestDto requestDto = new GoodsRequestDto();
        requestDto.setGoodsName("이승엽빠따");
        requestDto.setGoodsContent("팜");
        requestDto.setGoodsImg("sample.png");
        requestDto.setGoodsPrice(30000);
        goodsService.createGoods("aaa",requestDto);

        requestDto.setGoodsName("박찬호 사인볼");
        requestDto.setGoodsContent("팜");
        requestDto.setGoodsImg("sample.png");
        requestDto.setGoodsPrice(100000);
        goodsService.createGoods("bbb",requestDto);

        requestDto.setGoodsName("롯데유니폼싸게구함");
        requestDto.setGoodsContent("구함");
        requestDto.setGoodsImg("sample.png");
        requestDto.setGoodsPrice(2300);
        goodsService.createGoods("ccc",requestDto);

        requestDto.setGoodsName("야구장예약");
        requestDto.setGoodsContent("양도 또는 팜");
        requestDto.setGoodsImg("sample.png");
        requestDto.setGoodsPrice(57000);
        goodsService.createGoods("aaa",requestDto);

        requestDto.setGoodsName("11월25일 경기장 표팔아요");
        requestDto.setGoodsContent("s급 석 2명");
        requestDto.setGoodsImg("sample.png");
        requestDto.setGoodsPrice(90000);
        goodsService.createGoods("bbb",requestDto);
    }

    @Transactional
    public void commentSet()
    {
        GroupCommentRequestDto com1 = new GroupCommentRequestDto();
        com1.setComment("aaa가 작성한 그룹댓글 내용");
        Group groud = groupRepository.findById(137L).orElseThrow(
                ()->new IllegalArgumentException("그룹정보못찾음")
        );
        User user = userRepository.findByUsername("aaa").orElseThrow(
                ()->new IllegalArgumentException("로그인정보못찾음")
        );
        groupCommentService.createComment(com1,groud.getGroupId(),user);

        com1.setComment("bbb가 작성한 그룹댓글내용");
        groud = groupRepository.findById(137L).orElseThrow(
                ()->new IllegalArgumentException("그룹정보못찾음")
        );
        user = userRepository.findByUsername("bbb").orElseThrow(
                () -> new IllegalArgumentException("로그인정보못찾음")
        );
        groupCommentService.createComment(com1,groud.getGroupId(),user);

        com1.setComment("ccc가 작성한 그룹댓글내용");
        groud = groupRepository.findById(137L).orElseThrow(
                ()->new IllegalArgumentException("그룹정보못찾음")
        );
        user = userRepository.findByUsername("ccc").orElseThrow(
                () -> new IllegalArgumentException("로그인정보못찾음")
        );
        groupCommentService.createComment(com1,groud.getGroupId(),user);

        GoodsCommentRequestDto com2 = new GoodsCommentRequestDto();
        com2.setComment("aaa가 작성한 굿즈댓글내용");
        Goods goods = goodsRepository.findById(141L).orElseThrow(
                ()->new IllegalArgumentException("굿즈정보못찾음")
        );
        goodsCommentService.createComment("aaa",com2,goods.getId());

        com2.setComment("bbb가 작성한 굿즈댓글내용");
        goods = goodsRepository.findById(141L).orElseThrow(
                () -> new IllegalArgumentException("굿즈정보못찾음")
        );
        goodsCommentService.createComment("bbb",com2,goods.getId());

        com2.setComment("ccc가 작성한 굿즈댓글내용");
        goods = goodsRepository.findById(141L).orElseThrow(
                () -> new IllegalArgumentException("굿즈정보못찾음")
        );
        goodsCommentService.createComment("bbb",com2,goods.getId());

    }
}
