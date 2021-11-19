package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long>{
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByUserid(String userId);
    Optional<User> findByPhoneNumber(String phonenumber);
    //    boolean existByUsername(String username);
    <T> Optional<T> findByUserid(String userId, Class <T>type);

    Optional<User> findById(Long Id);

}
