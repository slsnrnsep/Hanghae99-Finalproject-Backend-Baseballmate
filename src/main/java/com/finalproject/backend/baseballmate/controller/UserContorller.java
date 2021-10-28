package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.HeaderDto;
import com.finalproject.backend.baseballmate.requestDto.MyteamRequestDto;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.responseDto.UserResponseDto;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserContorller {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/user/signup")
    public void registerUser(@RequestBody UserRequestDto userRequestDto){
        userService.passwordCheck(userRequestDto.getPassword());
//        userService.UsernameChk(userRequestDto.getUsername());
        userService.useridCheck(userRequestDto.getUserid());
        userService.registerUser(userRequestDto);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody UserRequestDto userRequestDto) {
        User user = userRepository.findByUserid(userRequestDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(user.getUserid(), user.getId(),user.getUsername());
    }

    @PostMapping("/user/myteam")
    public void selectMyteam(@RequestBody MyteamRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }

        User user = userRepository.findByUsername(userDetails.getUser().getUsername())
                .orElseThrow(()-> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다."));

        user.setMyselectTeam(requestDto.getMyteam());

        userRepository.save(user);

//        return "구단 등록에 성공하였습니다";
    }

    //카카오 로그인 api로 코드를 받아옴
    @GetMapping("/user/kakao/callback")
    @ResponseBody
    public HeaderDto kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        return userService.kakaoLogin(code);
    }



}
