package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.UserRequestDto;
import com.finalproject.backend.baseballmate.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

//    public UserResponseDto UsernameChk(String username){
//        boolean isExist = userRepository.existByUsername(username);
//
//        if (isExist) {
//            return new UserResponseDto(false, "중복된 ID가 존재합니다", 200);
//        } else {
//            return new UserResponseDto(true, "사용 가능한 ID 입니다.", 200);
//        }
//    }
}
