package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;

    @Transactional
    public void createGoods(String username, GoodsRequestDto requestDto) {
        Goods goods = new Goods(username, requestDto);
        goodsRepository.save(goods);
    }

    public List<Goods> getGoods() {
        List<Goods> goodsList = goodsRepository.findAll();
        return goodsList;
    }

}
