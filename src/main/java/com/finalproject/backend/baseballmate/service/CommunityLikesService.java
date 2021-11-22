package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Community;
import com.finalproject.backend.baseballmate.model.CommunityLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.CommunityLikesRepository;
import com.finalproject.backend.baseballmate.repository.CommunityRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.CommunityLikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityLikesService {

    private final CommunityLikesRepository communityLikesRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    @Transactional
    public boolean communityLiked(Long communityId, CommunityLikesRequestDto requestDto, UserDetailsImpl userDetails)
    {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );

        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new IllegalArgumentException("모임을 찾을 수 없습니다.")
        );

        if(requestDto.getIsLiked().equals("true")){
            CommunityLikes likes = communityLikesRepository.findByCommunitylikesCommunityIdAndUserId(community.getCommunityId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );

            user.deleteCommunityLikes(likes);
            community.deleteLikes(likes);
            communityLikesRepository.delete(likes);
            return false;
        }else {
            if(communityLikesRepository.findByCommunitylikesCommunityIdAndUserId(community.getCommunityId(), user.getId()).isPresent())
            {
                return true;
            }
            CommunityLikes likes = communityLikesRepository.save(new CommunityLikes(community, user));
            user.addCommunityLikes(likes);
            community.addLikes(likes);
            return true;
        }
    }
}
