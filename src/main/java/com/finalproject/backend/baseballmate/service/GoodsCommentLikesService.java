package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.GoodsComment;
import com.finalproject.backend.baseballmate.model.GoodsCommentLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsCommentLikesRepository;
import com.finalproject.backend.baseballmate.repository.GoodsCommentRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsCommentLikesService {

    private final GoodsCommentLikesRepository goodsCommentLikesRepository;
    private final UserRepository userRepository;
    private final GoodsCommentRepository goodsCommentRepository;
    private final AlarmService alarmService;

    @Transactional
    public boolean goodsCommentLikes(Long goodscommentId, GoodsLikesReqeustDto reqeustDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다")
        );
        GoodsComment goodsComment = goodsCommentRepository.findById(goodscommentId).orElseThrow(
                () -> new IllegalArgumentException("굿즈가 존재하지 않습니다.")
        );

        if(reqeustDto.getIsLiked().equals("true")){
            GoodsCommentLikes likes = goodsCommentLikesRepository.findByGoodsCommentIdAndUserId(goodsComment.getId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("좋아요 이력이 없습니다")
            );
            user.deleteGoodsCommentLikes(likes);
            goodsComment.deleteCommentLikes(likes);
            goodsCommentLikesRepository.delete(likes);
            return false;
        }else{
            if(goodsCommentLikesRepository.findByGoodsCommentIdAndUserId(goodsComment.getId(), user.getId()).isPresent())
            {
                return true;
            }
            GoodsCommentLikes likes = goodsCommentLikesRepository.save(new GoodsCommentLikes(goodsComment, user));
            user.addGoodsCommentLikes(likes);
            goodsComment.addCommentLikes(likes);

            User alarmuser = userRepository.findById(goodsComment.getCommentUserIndex()).orElseThrow(
                    () -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다")
            );
            alarmService.alarmMethod(alarmuser,user.getUsername(),goodsComment.getComment(),"댓글","좋아요를 표시했습니다!",goodsComment.getGoods().getId());
            return true;
        }
    }

}
