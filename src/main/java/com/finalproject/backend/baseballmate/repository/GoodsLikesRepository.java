package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsLikesRepository extends JpaRepository<GoodsLikes, Long> {
    Optional<GoodsLikes> findByGoodsIdAndUserId(Long goodsId, Long UserId);
    List<GoodsLikes> findAllByUserId(Long UserId);
}
