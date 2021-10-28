package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupCommentRepository;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupCommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class GroupCommentService {
    private final GroupCommentRepository groupCommentRepository;
    private final GroupRepository groupRepository;

    // 모임 게시글 내 댓글 생성
    // 우선 파라미터로 게시글 id 가져옴 -> 후에 연관관계 이용해서 리팩토링 필요
    @Transactional
   public void createComment(GroupCommentRequestDto groupCommentRequestDto, Long groupId, User logineduser) {
        // loginuser의 유저네임 빼기
        String loginedUsername = logineduser.getUsername();
        // loginuser의 유저id 뽑기
        Long loginedUserId = logineduser.getId();
        // groupentity에서 groupid에 맞는 모임 가져오기
        Group group = groupRepository.findByGroupId(groupId);
        // 파라미터들로 groupcomment를 추가하기 위한 인스턴스 생성
        GroupComment groupComment = new GroupComment(groupCommentRequestDto, group, loginedUsername, loginedUserId);
        // groupcomment 테이블에 데이터 저장
        groupCommentRepository.save(groupComment);
        // 연관관계가 있는 group 테이블의 댓글 리스트에 생성한 댓글 인스턴스 추가
        group.getGroupCommentList().add(groupComment);
   }
}
