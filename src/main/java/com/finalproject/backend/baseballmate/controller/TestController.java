package com.finalproject.backend.baseballmate.controller;


import com.finalproject.backend.baseballmate.model.MatchInfomation;
import com.finalproject.backend.baseballmate.service.MatchDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final MatchDataService matchDataService;

    @GetMapping("/api/kbodata")
    public List<MatchInfomation> kbodata() throws IOException {
        List<MatchInfomation> matchInfomationList = matchDataService.getKBODatas();

        return matchInfomationList;
    }
}
