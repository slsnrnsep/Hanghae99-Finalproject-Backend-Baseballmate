package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsLikesRepository;
import com.finalproject.backend.baseballmate.repository.TimeLineLikesRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.HeaderDto;
import com.finalproject.backend.baseballmate.requestDto.MyteamRequestDto;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.responseDto.LoginCheckResponseDto;
import com.finalproject.backend.baseballmate.responseDto.UserResponseDto;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.LoginResponseDto;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserContorller {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TimeLineLikesRepository timeLineLikesRepository;
    private final GoodsLikesRepository goodsLikesRepository;

    @PostMapping("/user/signup")
    public void registerUser(@RequestBody UserRequestDto userRequestDto){
        userService.passwordCheck(userRequestDto.getPassword());
//        userService.UsernameChk(userRequestDto.getUsername());
        userService.useridCheck(userRequestDto.getUserid());
        userService.registerUser(userRequestDto);

    }

    @PostMapping("/user/login")
    public LoginResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        User user = userRepository.findByUserid(userRequestDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtTokenProvider.createToken(user.getUserid(), user.getId(),user.getUsername()),user.getMyselectTeam());
        return loginResponseDto;
    }

    // 유저의 구단정보 보내기
    // 일단 myteam 정보만 보내주고 이후에 userresponsedto로 모든 정보를 보내줄 수 있음
    @PatchMapping("/users/{id}")
    public Map<String, String> updateUserInfo(@PathVariable Long id, @RequestBody UserRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 후에 이용하실 수 있습니다.");
        }

        UserResponseDto responseDto = userService.partialUpdate(id, requestDto);
        Map<String, String> myTeamResponse = new HashMap<>();
        myTeamResponse.put("myteam", responseDto.getMyteam());
        return myTeamResponse;
    }


    @PostMapping("/user/myteam")
    public MyteamRequestDto selectMyteam(@RequestBody MyteamRequestDto myteam, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }

        User user = userRepository.findByUserid(userDetails.getUser().getUserid())
                .orElseThrow(()-> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다."));

        if(myteam.getMyteam() == null)
        {
            throw new IllegalArgumentException("구단선택을 null로 했습니다");
        }

        user.setMyselectTeam(myteam.getMyteam());

        userRepository.save(user);

        MyteamRequestDto myteamRequestDto = new MyteamRequestDto(user.getMyselectTeam());
        return myteamRequestDto;
    }

    @PostMapping("/user/logincheck")
    public LoginCheckResponseDto loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 정보를 찾을 수 없습니다");
        }
        User user = userRepository.findByUsername(userDetails.getUser().getUsername())
                .orElseThrow(()-> new IllegalArgumentException("로그인 유저를 찾을 수 없습니다."));

        //프론트엔드 진식님 요청사항
        List<TimeLineLikes> TimeLineLikesList=timeLineLikesRepository.findAllByUserId(user.getId());
        List<Long> myTimeLineLikesList = new ArrayList<>();
        for(int i=0; i<TimeLineLikesList.size();i++) {
            myTimeLineLikesList.add(TimeLineLikesList.get(i).getId());
        }

        List<GoodsLikes> GoodsLikesList = goodsLikesRepository.findAllByUserId(user.getId());
        List<Long> myGoodsLikesList = new ArrayList<>();
        for(int i=0; i<GoodsLikesList.size();i++) {
            myGoodsLikesList.add(GoodsLikesList.get(i).getId());
        }

        LoginCheckResponseDto loginCheckResponseDto = new LoginCheckResponseDto(user.getUsername(),user.getMyselectTeam(),myTimeLineLikesList,myGoodsLikesList);

        return loginCheckResponseDto;
    }

    //카카오 로그인 api로 코드를 받아옴
    @GetMapping("/user/kakao/callback")
    @ResponseBody
    public HeaderDto kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        return userService.kakaoLogin(code);
    }



}
