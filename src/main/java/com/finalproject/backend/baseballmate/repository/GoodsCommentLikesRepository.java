package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GoodsCommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsCommentLikesRepository extends JpaRepository<GoodsCommentLikes, Long> {
    Optional<GoodsCommentLikes> findByGoodsCommentIdAndUserId(Long goodsId, Long UserId);
    List<GoodsCommentLikes> findByUserId(Long UserId);

}
