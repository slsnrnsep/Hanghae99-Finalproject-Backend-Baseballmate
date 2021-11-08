package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.GoodsLikesReqeustDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GroupCommentLikesService {

    private final GroupCommentLikesRepository groupCommentLikesRepository;
    private final UserRepository userRepository;
    private final GroupCommentRepository groupCommentRepository;

    @Transactional
    public boolean groupCommentLikes(Long groupcommentId, GoodsLikesReqeustDto reqeustDto, UserDetailsImpl userDetails)
    {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );
        GroupComment groupComment = groupCommentRepository.findById(groupcommentId).orElseThrow(
                () -> new IllegalArgumentException("모임이 존재하지 않습니다.")
        );

        if (reqeustDto.getIsLiked().equals("true")) {
            GroupCommentLikes likes = groupCommentLikesRepository.findByGroupComment_GroupCommentIdAndUserId(groupComment.getGroupCommentId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );

            user.deleteGroupCommentLikes(likes);
            groupComment.deleteLikes(likes);
            groupCommentLikesRepository.delete(likes);
            return false;
        } else {
            if(groupCommentLikesRepository.findByGroupComment_GroupCommentIdAndUserId(groupComment.getGroupCommentId(), user.getId()).isPresent())
            {
                return true;
            }

            GroupCommentLikes likes = groupCommentLikesRepository.save(new GroupCommentLikes(groupComment, user));
            user.addGroupCommentLikes(likes);
            groupComment.addLikes(likes);
            return true;
        }
    }
}
