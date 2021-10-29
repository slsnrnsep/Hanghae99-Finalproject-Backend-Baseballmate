package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository <Goods, Long>{
    List<Goods> findAllByOrderByCreatedAtDesc();
//    Goods findById(Long goodsId);
}
