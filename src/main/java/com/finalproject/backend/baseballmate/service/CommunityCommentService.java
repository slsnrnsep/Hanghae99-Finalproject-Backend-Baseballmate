package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Community;
import com.finalproject.backend.baseballmate.model.CommunityComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.CommunityCommentRepository;
import com.finalproject.backend.baseballmate.repository.CommunityRepository;
import com.finalproject.backend.baseballmate.requestDto.CommunityCommentRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
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
    public MsgResponseDto createComment(CommunityCommentRequestDto commentRequestDto, Long communityId, UserDetailsImpl userDetails) {

        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        User loginedUser = userDetails.getUser();

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

        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "댓글 등록 완료");
        return msgResponseDto;
    }


    @Transactional
    public MsgResponseDto updateCommunityComment(Long communityId, Long commentId, CommunityCommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

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
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
            return msgResponseDto;
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }

    @Transactional
    public MsgResponseDto deleteCommunityComment(Long commentId, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String commentUserId = "";

        CommunityComment communityComment = communityCommentRepository.findByCommentId(commentId);
        if(communityComment != null){
            commentUserId = communityComment.getCommentUserId();

            if(!loginedUserId.equals(commentUserId)){
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            communityCommentRepository.deleteById(commentId);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
            return msgResponseDto;
        }else {
            throw new NullPointerException("해당 댓글이 존재하지 않습니다.");
        }
    }
}
