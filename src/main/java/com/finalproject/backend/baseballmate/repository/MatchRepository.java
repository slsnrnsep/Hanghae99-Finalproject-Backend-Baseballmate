package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.MatchInfomation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchInfomation, Long> {

    List<MatchInfomation> findAllByOrderByMatchIdDesc();
    List<MatchInfomation> findAllByDatenum(String num);


}
