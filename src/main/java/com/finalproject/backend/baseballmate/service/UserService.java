package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.OAuth2.KakaoOAuth2;
import com.finalproject.backend.baseballmate.OAuth2.KakaoUserInfo;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.HeaderDto;
import com.finalproject.backend.baseballmate.requestDto.UserProfileRequestDto;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.responseDto.UserResponseDto;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private static final String Pass_Salt = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

//    @Value("${app.auth.tokenSecret}")
//    private String secretKey;

    public void registerUser(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        String userid = userRequestDto.getUserid();

//        Optional<User> check = userRepository.findByUsername(username);
        String pattern = "^[a-zA-Z0-9]*$";

        password = passwordEncoder.encode(userRequestDto.getPassword());

        User user = new User(userid, username, password);
        userRepository.save(user);

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
    public UserResponseDto partialUpdate(long id, final UserRequestDto requestDto) {
        Optional<User> oUser = userRepository.findById(id);
//        if (!oUser.isPresent())
//            return 0;
        User user = oUser.get();
        if (StringUtils.isNotBlank(requestDto.getUsername()))
            user.setUsername(requestDto.getUsername());
        if (StringUtils.isNotBlank(requestDto.getPassword()))
            user.setPassword(requestDto.getPassword());
        if (StringUtils.isNotBlank(requestDto.getMyteam()))
            user.setMyselectTeam(requestDto.getMyteam());
        User updatedUser = userRepository.save(user);

        UserResponseDto userResponseDto =
                new UserResponseDto(id, updatedUser.getUserid(), updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getMyselectTeam());
        return userResponseDto;
    }

    //user 프로필 사진 등록 및 변경
    @Transactional
    public void updateProfileImage(Long id, final UserProfileRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findById(id);

        User user = optionalUser.get();
        if (StringUtils.isNotBlank(requestDto.getProfileImage()))
            user.setPicture(requestDto.getProfileImage());
        userRepository.save(user);
    }

    public HeaderDto kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);


        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
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

        headerDto.setTOKEN(jwtTokenProvider.createToken(member.getUserid(), member.getId(), member.getUsername()));
//      headerDto.setTOKEN(jwtTokenProvider.createToken(email, member.getId(), member.getUsername()));
//        System.out.println(test);
        return headerDto;

    }

}
