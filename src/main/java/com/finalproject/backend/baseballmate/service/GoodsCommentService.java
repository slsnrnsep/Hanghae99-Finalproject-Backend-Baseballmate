package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.GoodsComment;
import com.finalproject.backend.baseballmate.repository.GoodsCommentRepository;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsCommentRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class GoodsCommentService {
    private final GoodsCommentRepository goodsCommentRepository;
    private final GoodsRepository goodsRepository;

    @Transactional
    public void createComment(String username, GoodsCommentRequestDto requestDto, Long goodsid) {
//        Goods goods = new Goods(username, requestDto);
//        goodsRepository.save(goods);
        Goods goods = goodsRepository.findById(goodsid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다")
        );
        GoodsComment goodsComment = new GoodsComment(username,requestDto,goods);
        goodsCommentRepository.save(goodsComment);

    }

    public void updateGoodsComment(String username, Long id, GoodsCommentRequestDto requestDto) {
        String writer = "";

        GoodsComment goodsComment = goodsCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아아디입니다")
        );

        if(goodsComment != null){
            writer = goodsComment.getUserName();

            if(!username.equals(writer)){
                throw new IllegalArgumentException("수정권한이 없습니다");
            }
            goodsComment.updateComment(requestDto);
            goodsCommentRepository.save(goodsComment);
        }else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    public void deleteComment(Long id, UserDetailsImpl userDetails) {
        String loginUser = userDetails.getUsername();
        String writer = "";

        GoodsComment goodsComment = goodsCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다")
        );
        if(goodsComment != null){
            writer = goodsComment.getUserName();

            if(!loginUser.equals(writer)){
                throw new IllegalArgumentException("삭제권한이 없습니다");
            }
            goodsCommentRepository.deleteById(id);
        }else{
            throw new NullPointerException("해당 게시글이 존재하지 않습니다");
        }
    }
}
