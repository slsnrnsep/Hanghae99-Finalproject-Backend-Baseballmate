package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository <Goods, Long>{

}
