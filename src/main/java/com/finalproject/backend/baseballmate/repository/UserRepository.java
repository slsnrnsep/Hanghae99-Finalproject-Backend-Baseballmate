package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByUserid(String userId);
//    boolean existByUsername(String username);

}
