package com.finalproject.backend.baseballmate.service;


import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GroupLikesService {
    private final GroupLikesRepository groupLikesRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public boolean groupLikes(Long groupId, LikesRequestDto reqeustDto, UserDetailsImpl userDetails)
    {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("모임이 존재하지 않습니다.")
        );

        if (reqeustDto.getIsLiked().equals("true")) {
            GroupLikes likes = groupLikesRepository.findByGrouplikesGroupIdAndUserId(group.getGroupId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );

            user.deleteGroupLikes(likes);
            group.deleteLikes(likes);
            groupLikesRepository.delete(likes);
            return false;
        } else {
            if(groupLikesRepository.findByGrouplikesGroupIdAndUserId(group.getGroupId(), user.getId()).isPresent())

            {
                return true;
            }
            GroupLikes likes = groupLikesRepository.save(new GroupLikes(group, user));
            user.addGroupLikes(likes);

            group.addLikes(likes);
            return true;
        }

    }
}
