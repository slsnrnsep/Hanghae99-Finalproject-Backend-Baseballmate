package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.security.OAuth2.KakaoOAuth2;
import com.finalproject.backend.baseballmate.security.OAuth2.KakaoUserInfo;
import com.finalproject.backend.baseballmate.model.AddressEnum;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.*;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.utils.MD5Generator;
import com.finalproject.backend.baseballmate.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final JwtTokenProvider jwtTokenProvider;
    private final PhoneService phoneService;
    private final AuthenticationManager authenticationManager;
    private static final String Pass_Salt = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    private String commonPath = "/images"; // 파일 저장할 기본 경로 변수 설정, 초기화
    private final AlarmService alarmService;

//    @Value("${app.auth.tokenSecret}")
//    private String secretKey;

    public void registerUser(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        String userid = userRequestDto.getUserid();
        String myteam = userRequestDto.getMyteam();

        Optional<User> check = userRepository.findByUsername(username);
        String pattern = "^[a-zA-Z0-9]*$";

        password = passwordEncoder.encode(userRequestDto.getPassword());


        User user = userRepository.findByPhoneNumber(userRequestDto.getPhonenumber()+"#"+userRequestDto.getRanNum()).orElseThrow(
                ()-> new IllegalArgumentException("휴대폰에 맞는 유저정보를 찾을 수 없습니다.")
        );
        user.setUserid(userid);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoneNumber(userRequestDto.getPhonenumber()+"#"+user.getRanNum());
        user.setAddress("전국");
        user.setPicture("sample.png");

//        User user = new User(userid,username,password, userRequestDto.getPhonenumber());

        userRepository.save(user);

//        User loginedUser = userDetails.getUser();
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        String signupAlarm = "안녕하세요 " + user.getUsername() + "님! 가입을 환영합니다";
        alarmRequestDto.setUserId(user.getId());
        alarmRequestDto.setContents(signupAlarm);
        alarmRequestDto.setAlarmType("Normal");
        alarmService.createAlarm(alarmRequestDto);
    }

    public void passwordCheck(String password) {
        final int MIN = 8;
        final int MAX = 16;
        final String pattern = "(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*()'/_=+{}-])[0-9a-zA-Z!@#$%^&*()'/_=+{}-]{"+MIN+","+MAX+"}$";
        if (!Pattern.matches(pattern, password)) {
            throw new IllegalArgumentException("비밀번호 입력 오류");
        }
    }

    //username으로 들어오는 email 체크
    public void useridCheck(String userid) {
        final String pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if (!Pattern.matches(pattern, userid)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
        User found = userRepository.findByUserid(userid).orElse(null);
        if (found != null) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }

    // user정보 부분 변경
    @Transactional
    public List<Map<String, String>> partialUpdateUserInfo(long id, MultipartFile file, UserUpdateRequestDto requestDto, UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        List<Map<String, String>> responseList = new ArrayList<>();
        Map<String,String> response = new HashMap<>();

        // 로그인 유무 판별
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 이용자만 이용하실 수 있습니다.");
        }

        // userinx에 맞는 user 객체 가져오기
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();

        // Dto로 온 정보를 유저 객체에 새롭게 저장
        if (StringUtils.isNotBlank(requestDto.getUsername())) {
            user.setUsername(requestDto.getUsername());
            response.put("username", user.getUsername());
            responseList.add(0, response);
        }
        if (StringUtils.isNotBlank(requestDto.getPassword())) {
            user.setPassword(requestDto.getPassword());
            response.put("password", user.getPassword());
            responseList.add(0, response);
        }

        if (StringUtils.isNotBlank(requestDto.getMyteam())) {
            user.setMyselectTeam(requestDto.getMyteam());
            response.put("myteam", user.getMyselectTeam());
            responseList.add(0, response);
        }

        if (StringUtils.isNotBlank(requestDto.getSelfIntroduction())){
            user.setSelfIntroduction(requestDto.getSelfIntroduction());
            response.put("selfIntroduction", user.getSelfIntroduction());
            responseList.add(0, response);
        }
        if (StringUtils.isNotBlank(requestDto.getAddress())) {
            user.setAddress(requestDto.getAddress());
            response.put("address", user.getAddress());
            responseList.add(0, response);
        }


        User updatedUser = userRepository.save(user);

        // 이미지 파일 확인하기
        // 이미지 파일이 들어온 게 있으면 변경, dto만 있으면 dto값들만 변경, 둘 다 있으면 이미지 파일, dto 내용 모두 변경

        if (file != null) {
            String origFilename = file.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString() + "jpg";

            String savePath = "/home/ubuntu/app/travis" + commonPath;

            // 파일이 저장되는 폴더가 없을 경우 폴더 생성
            if (!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            // 이미지 파일 저장
            String filePath = savePath + "/" + filename;
            try{
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            updatedUser.setPicture(filename);
            response.put("picture", updatedUser.getPicture());
            responseList.add(0, response);
        }

        return responseList;
    }


    public HeaderDto kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);

        String[] ranpre = {"살아가는","눈물의","센치한","고난의","무야호","해맑은","야구하는","내가바로","도전하는","인상적인"};
        String[] ransuf = {"연어","고등어","파도","조각상","키보드","피카츄","프로도","두부","댕댕이","반죽" };
        Random random = new Random();


        Long kakaoId = userInfo.getId();
        String nickname = ranpre[random.nextInt(10)+1]+ransuf[random.nextInt(10)+1];
        String password = kakaoId + Pass_Salt;


        //nullable = true
        String picture = userInfo.getPicture();
        String email = userInfo.getEmail();

        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
        if (kakaoUser != null) {
//            kakaoUser.update(nickname, picture);
            userRepository.save(kakaoUser);
        } else { // 새 유저
            kakaoUser = User.builder()
                    .userid(email)
                    .username(nickname)
                    .picture(picture)
                    .kakaoId(kakaoId)
                    .password(password)
                    .build();
            userRepository.save(kakaoUser);

        }
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);

        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        HeaderDto headerDto = new HeaderDto();

        // 로그인 처리 후 해당 유저 정보를 바탕으로 JWT토큰을 발급하고 해당 토큰을 Dto에 담아서 넘김
        User member = userRepository.findByKakaoId(kakaoId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        headerDto.setTOKEN(jwtTokenProvider.createToken(member.getUserid(), member.getId(), member.getUsername(), member.getPicture()));
//      headerDto.setTOKEN(jwtTokenProvider.createToken(email, member.getId(), member.getUsername()));
//        System.out.println(test);
        return headerDto;

    }

    public List<AddressEnum> getAddressEnumList(UserDetailsImpl userDetails) {
        if (userDetails == null)
        {
            throw new IllegalArgumentException("로그인 후 이용 가능합니다.");
        }
        AddressEnum[] values = AddressEnum.values();
        List<AddressEnum> addressEnums = new ArrayList<>();
        addressEnums.addAll(Arrays.asList(values));

        return addressEnums;
    }

    // 채팅방 관련.
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));
    }
}

// 클래스 묶는 중괄호 밖으로 빼놓음
//    public void confirmNumChk(UserDetailsImpl userDetails, PhoneRequstDto requstDto) {
//
//       User user = userRepository.findByUserid(userDetails.getUser().getUserid()).orElseThrow(
//                () -> new IllegalArgumentException("사용자 정보가 일치하지 않습니다")
//        );
//
//        String ranNumChk = phoneService.sendMessage(requstDto);
//        if(requstDto.getRanNum() == (ranNumChk)){
//
//            user.setRanNum(requstDto.getRanNum());
//            userRepository.save(user);
//        }else{
//            throw new IllegalArgumentException("인증번호가 일치하지 않습니다");
//        }
//    }