package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.HeaderDto;
import com.finalproject.backend.baseballmate.requestDto.MyteamRequestDto;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.requestDto.UserUpdateRequestDto;
import com.finalproject.backend.baseballmate.responseDto.LoginCheckResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.responseDto.LoginResponseDto;
import com.finalproject.backend.baseballmate.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Api(tags = {"0. 유저"}) // Swagger
public class UserContorller {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TimeLineLikesRepository timeLineLikesRepository;
    private final GoodsLikesRepository goodsLikesRepository;
    private final GroupLikesRepository groupLikesRepository;
    private final GroupCommentLikesRepository groupCommentLikesRepository;
    private final ScreenLikesRepository screenLikesRepository;
    private final ScreenCommentLikesRepository screenCommentLikesRepository;

    @PostMapping("/user/signup")
    @ApiOperation(value = "일반유저 회원 가입", notes = "일반유저 회원 가입")
    public MsgResponseDto registerUser(@RequestBody UserRequestDto userRequestDto) {
        try
        {
            userService.passwordCheck(userRequestDto.getPassword());
            userService.useridCheck(userRequestDto.getUserid());
            userService.registerUser(userRequestDto);
            MsgResponseDto msgResponseDto = new MsgResponseDto("Success","회원가입이 완료되었습니다.");
            return msgResponseDto;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");

        }
    }


    @PostMapping("/user/login")
    @ApiOperation(value = "일반유저 로그인", notes = "일반유저 로그인")
    public LoginResponseDto login(@RequestBody UserRequestDto userRequestDto)
    {
        User user = userRepository.findByUserid(userRequestDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword()))
        {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtTokenProvider.createToken(user.getUserid(), user.getId(),user.getUsername(), user.getPicture()),user.getMyselectTeam());
        return loginResponseDto;
    }


    //patchmapping 일반화(구단 정보, 주소, 자기소개 등록 모두 가능)
    @RequestMapping(value = "/users/{id}/legacy", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "(legacy)유저 정보수정", notes = "(legacy)유저 정보수정")
    public List<Map<String, String>> updateUserInfo(
            @PathVariable("id") Long id,
            @RequestPart(required = false, value = "file") MultipartFile file,
            UserUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {


        List<Map<String, String>> responseList = userService.partialUpdateUserInfo(id, file, requestDto, userDetails);

        return responseList;
    }

    //patchmapping 일반화(구단 정보, 주소, 자기소개 등록 모두 가능)
    @PutMapping("/users/{id}")
    @ApiOperation(value = "유저 정보수정", notes = "유저 정보수정")
    public List<Map<String, String>> updateUserInfo2(
            @PathVariable("id") Long id, @RequestBody UserUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        List<Map<String, String>> responseList = userService.partialUpdateUserInfo2(id, requestDto, userDetails);

        return responseList;

    }

    @PostMapping("/user/myteam")
    @ApiOperation(value = "유저 구단정보 수정", notes = "유저 구단정보 수정")
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

    //프론트 요청
    @PostMapping("/user/logincheck")
    @ApiOperation(value = "유저 로그인 체크", notes = "유저 로그인 체크")
    public LoginCheckResponseDto loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        String usertype = "";
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 정보를 찾을 수 없습니다");
        }

        User user = userRepository.findByUsername(userDetails.getUser().getUsername())
                .orElseThrow(()-> new IllegalArgumentException("로그인 유저를 찾을 수 없습니다."));

        if(user.getKakaoId() == null)
        {
            usertype = "normal";
        }
        else
        {
            usertype = "kakao";
        }

        //프론트엔드 진식님 요청사항
        List<TimeLineLikes> TimeLineLikesList=timeLineLikesRepository.findAllByUserId(user.getId());
        List<Long> myTimeLineLikesList = new ArrayList<>();
        for(int i=0; i<TimeLineLikesList.size();i++)
        {
            myTimeLineLikesList.add(TimeLineLikesList.get(i).getTimeLine().getId());
        }
        List<GoodsLikes> GoodsLikesList = goodsLikesRepository.findAllByUserId(user.getId());
        List<Long> myGoodsLikesList = new ArrayList<>();
        for(int i=0; i<GoodsLikesList.size();i++)
        {
            myGoodsLikesList.add(GoodsLikesList.get(i).getGoods().getId());
        }
        List<GroupLikes> groupLikesList = groupLikesRepository.findAllByUserId(user.getId());
        List<Long> myGroupLikesList = new ArrayList<>();
        for (int i=0; i<groupLikesList.size();i++)
        {
            myGroupLikesList.add(groupLikesList.get(i).getGrouplikes().getGroupId());
        }
        List<GroupCommentLikes> groupCommentLikesList = groupCommentLikesRepository.findAllByUserId(user.getId());
        List<Long> myGroupCommentLikesList = new ArrayList<>();
        for (int i=0; i<groupCommentLikesList.size();i++)
        {
            myGroupCommentLikesList.add(groupCommentLikesList.get(i).getGroupComment().getGroupCommentId());
        }
        List<ScreenLikes> screenLikesList = screenLikesRepository.findAllByUserId(user.getId());
        List<Long> myScreenLikesList = new ArrayList<>();
        for (int i=0; i<screenLikesList.size();i++)
        {
            myScreenLikesList.add(screenLikesList.get(i).getScreenlikes().getScreenId());
        }
        List<ScreenCommentLikes> screenCommentLikesList = screenCommentLikesRepository.findAllByUserId(user.getId());
        List<Long> myScreenCommentLikesList = new ArrayList<>();
        for (int i=0; i<screenCommentLikesList.size();i++)
        {
            myScreenCommentLikesList.add(screenCommentLikesList.get(i).getScreenComment().getScreenCommentId());
        }

        LoginCheckResponseDto loginCheckResponseDto = new LoginCheckResponseDto(user.getId(),user.getUserid(), user.getUsername(),user.getMyselectTeam(),user.getPicture(),user.getAddress(),usertype, user.getSelfIntroduction(),myTimeLineLikesList,myGoodsLikesList,myGroupLikesList,myGroupCommentLikesList,myScreenLikesList,myScreenCommentLikesList);



        return loginCheckResponseDto;
    }

    //카카오 로그인 api로 코드를 받아옴
    @GetMapping("/user/kakao/callback")
    @ApiOperation(value = "카카오유저 로그인", notes = "카카오유저 로그인")
    @ResponseBody
    public HeaderDto kakaoLogin(@RequestParam(value = "code", required = false) String code)
    {
        return userService.kakaoLogin(code);
    }

}

