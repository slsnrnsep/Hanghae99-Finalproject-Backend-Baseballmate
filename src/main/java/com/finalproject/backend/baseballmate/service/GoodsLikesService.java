package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsLikesRepository;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsLikesService {

    private final GoodsLikesRepository goodsLikesRepository;
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;

    @Transactional
    public boolean goodsLiked(Long goodsId, GoodsLikesReqeustDto goodsLikesReqeustDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다")
        );
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );
        if(goodsLikesReqeustDto.getIsLiked().equals("true")){
            GoodsLikes goodsLikes = goodsLikesRepository.findByGoodsIdAndUserId(goods.getId(), user.getId()).orElseThrow(
                    () ->new IllegalArgumentException("해당 굿즈의 좋아요 이력이 없습니다.")
            );
            user.deleteGoodsLikes(goodsLikes);
            goods.deleteGoodsLikes(goodsLikes);
            goodsLikesRepository.delete(goodsLikes);
            return false;
        }else{
            if(goodsLikesRepository.findByGoodsIdAndUserId(goods.getId(), user.getId()).isPresent())
            {
                return true;
            }
            GoodsLikes goodsLikes = goodsLikesRepository.save(new GoodsLikes(goods, user));
            user.addGoodsLikes(goodsLikes);
            goods.addGoodsLikes(goodsLikes);
            return true;
        }

    }
}
