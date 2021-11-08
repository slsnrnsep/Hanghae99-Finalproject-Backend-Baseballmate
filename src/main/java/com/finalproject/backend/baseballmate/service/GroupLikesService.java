package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupLikesRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupLikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GroupLikesService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupLikesRepository groupLikesRepository;

    @Transactional
    public boolean GroupLiked(Long groupId, GroupLikesRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다")
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("그룹이 존재하지 않습니다")
        );
        if(requestDto.getIsLiked().equals("true")){
            GroupLikes likes = groupLikesRepository.findByGroupGroupIdAndUserId(group.getGroupId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 그룹에 찜한 이력이 없습니다")
            );
            user.deleteGroupLiktes(likes);
            group.deleteGroupLikes(likes);
            groupLikesRepository.delete(likes);
            return false;
        }else{
            if(groupLikesRepository.findByGroupGroupIdAndUserId(group.getGroupId(), user.getId()).isPresent())
            {
                return true;
            }
            GroupLikes likes = groupLikesRepository.save(new GroupLikes(group, user));
            user.addGroupLikes(likes);
            group.addGroupLikes(likes);
            return true;
        }
    }
}
