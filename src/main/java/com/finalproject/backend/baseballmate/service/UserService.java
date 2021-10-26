package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.OAuth2.KakaoOAuth2;
import com.finalproject.backend.baseballmate.OAuth2.KakaoUserInfo;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.HeaderDto;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.responseDto.UserResponseDto;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Optional<User> check = userRepository.findByUsername(username);
        String pattern = "^[a-zA-Z0-9]*$";

        password = passwordEncoder.encode(userRequestDto.getPassword());

        User user = new User(userid, username, password);
        userRepository.save(user);

    }

    public HeaderDto kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);


        Long kakaoId = userInfo.getId();
//        String nickname = userInfo.getNickname();
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
                    .email(email)
                    .picture(picture)
                    .kakaoId(kakaoId)
                    .password(password)
                    .build();
            userRepository.save(kakaoUser);

        }
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HeaderDto headerDto = new HeaderDto();

        // 로그인 처리 후 해당 유저 정보를 바탕으로 JWT토큰을 발급하고 해당 토큰을 Dto에 담아서 넘김
        User member = userRepository.findByKakaoId(kakaoId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        headerDto.setTOKEN(jwtTokenProvider.createToken(email, member.getId(), member.getUsername()));
        return headerDto;

    }

}

//    public UserResponseDto UsernameChk(String username){
//        boolean isExist = userRepository.existByUsername(username);
//
//        if (isExist) {
//            return new UserResponseDto(false, "중복된 ID가 존재합니다", 200);
//        } else {
//            return new UserResponseDto(true, "사용 가능한 ID 입니다.", 200);
//        }
//    }
