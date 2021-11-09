//package com.finalproject.backend.baseballmate;
//
//import com.finalproject.backend.baseballmate.model.MatchInfomation;
//import com.finalproject.backend.baseballmate.service.MatchDataService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class jsouptest {
//
//    @Autowired
//    MatchDataService matchDataService;
//
//    @Test
//    public void KBO크롤링동작테스트() throws IOException {
//
//        // given
//        List<MatchInfomation> matchInfomationList = new ArrayList<>();
//
//        // when
//        matchInfomationList = matchDataService.getKBODatas();
//
////        // then
////       assertThat(matchInfomationList.get(0).getDate()).isEqualTo("10.1 (금)");
////        Assert.assertThat(matchInfomationList.get(0).getLocation()).isEqualTo("사직");
//
//    }
//}
