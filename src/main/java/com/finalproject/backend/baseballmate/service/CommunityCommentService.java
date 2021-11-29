package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Community;
import com.finalproject.backend.baseballmate.model.CommunityComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.CommunityCommentRepository;
import com.finalproject.backend.baseballmate.repository.CommunityRepository;
import com.finalproject.backend.baseballmate.requestDto.CommunityCommentRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {

    private final CommunityRepository communityRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final AlarmService alarmService;

    @Transactional
    public void createComment(CommunityCommentRequestDto commentRequestDto, Long communityId, User loginedUser) {
        // loginuser의 유저네임, 이메일 아이디 빼기
        String loginedUsername = loginedUser.getUsername();
        String loginedUserId = loginedUser.getUserid();
        // loginuser의 유저index 뽑기
        Long loginedUserIndex = loginedUser.getId();
        // loginuser의 유저 이미지 빼기
        String loginedUserPicture = loginedUser.getPicture();

        Community community = communityRepository.findByCommunityId(communityId);
        CommunityComment communityComment = new CommunityComment(commentRequestDto, community,loginedUserIndex,loginedUserId, loginedUsername,loginedUserPicture);
        communityCommentRepository.save(communityComment);
        community.getCommunityCommentList().add(communityComment);

        alarmService.alarmMethod(community.getCreatedUser(),loginedUsername,community.getContent(),"커뮤니티","댓글을 남기셨습니다.",communityId,"community");
    }


    @Transactional
    public void updateCommunityComment(Long communityId, Long commentId, CommunityCommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String commentUserId = "";

        CommunityComment communityComment = communityCommentRepository.findByCommentId(commentId);
        if(communityComment != null){
            commentUserId = communityComment.getCommentUserId();

            if(!loginedUserId.equals(commentUserId)){
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
            communityComment.updateCommunityComment(commentRequestDto);
            communityCommentRepository.save(communityComment);
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void deleteCommunityComment(Long commentId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String commentUserId = "";

        CommunityComment communityComment = communityCommentRepository.findByCommentId(commentId);
        if(communityComment != null){
            commentUserId = communityComment.getCommentUserId();

            if(!loginedUserId.equals(commentUserId)){
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            communityCommentRepository.deleteById(commentId);
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
}
