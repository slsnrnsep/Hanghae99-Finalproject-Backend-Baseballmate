package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GoodsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsCommentRepository extends JpaRepository<GoodsComment, Long> {
    List<GoodsComment> findAllByGoodsId(Long goodsId);
    List<GoodsComment> findAllByGoods_IdOrderByModifiedAtAsc(Long goodsId);

}
