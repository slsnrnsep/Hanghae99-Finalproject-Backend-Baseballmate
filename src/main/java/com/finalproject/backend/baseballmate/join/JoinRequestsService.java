package com.finalproject.backend.baseballmate.join;

import com.finalproject.backend.baseballmate.chat.*;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.AlarmRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.AlarmService;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinRequestsService {

    private final GroupRepository groupRepository;
    private final JoinRequestsRepository joinRequestsRepository;
    private final UserRepository userRepository;
    private final AllChatInfoRepository allChatInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AllChatInfoQueryRepository allChatInfoQueryRepository;
    private final AlarmService alarmService;
    private final GroupService groupService;
    //유저 신청정보 저장
    public String requestJoin(UserDetailsImpl userDetails, Long postId) {
        Long userId = userDetails.getUser().getId();
        // 신청하려는 방과 자신의 아이디가 이미 JoinRequests DB에 있는지 확인
        if (joinRequestsRepository.findByUserIdAndPostId(userId, postId) == null) {
            Group group = groupRepository.findByGroupId(postId);
            User user = group.getCreatedUser();
            Long ownUserId = user.getId();
            JoinRequests joinRequests = new JoinRequests(userId, postId, ownUserId);
            Long roomId = group.getChatRoom().getId();
            // 신청하려는 방과 자신의 아이디가 이미 AllChatInfo DB에 있는지 확인
            if (allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userId) == null) {
                joinRequestsRepository.save(joinRequests);
                AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
                String signupAlarm = user.getUsername() + "님! "+userDetails.getUser().getUsername()+"님이 내가 만든 모임:" +group.getTitle()+" 에 참여신청을 하셨습니다.";
                alarmRequestDto.setUserId(user.getId());
                alarmRequestDto.setContents(signupAlarm);
                alarmService.createAlarm(alarmRequestDto);
                return "신청완료";
            } else {
                return "이미 속해있는 채팅방입니다";
            }
        } else {
            return "이미 신청한 글입니다";
        }
    }

    //유저 신청정보 불러오기
    public List<UserInfoAndPostResponseDto> requestJoinList(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JoinRequests> joinRequestsList = joinRequestsRepository.findByOwnUserId(userId);
        List<UserInfoAndPostResponseDto> userInfoAndPostResponseDtoList = new ArrayList<>();

        for (JoinRequests joinRequests : joinRequestsList) {

            User userInfoMapping = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                    ()->  new IllegalArgumentException("회원이 아닙니다.")
            );

            Group post = groupRepository.findByGroupId(joinRequests.getPostId());

            UserInfoAndPostResponseDto userInfoAndPostResponseDto = UserInfoAndPostResponseDto.builder()
                    .userId(userInfoMapping.getId())
                    .username(userInfoMapping.getUsername())
                    .profileImg(userInfoMapping.getPicture())
                    .postTitle(post.getTitle())
                    .joinRequestId(joinRequests.getId())
                    .build();
            userInfoAndPostResponseDtoList.add(userInfoAndPostResponseDto);
        }

        return userInfoAndPostResponseDtoList;
    }

    // 신청 승인 대기 리스트
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinList(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JoinRequests> joinRequestsList = joinRequestsRepository.findByUserId(userId);
        List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinResponseDtoList = new ArrayList<>();
        for (JoinRequests joinRequests : joinRequestsList) {
            Group post = groupRepository.getById(joinRequests.getPostId());
            MyAwaitRequestJoinResponseDto myAwaitRequestJoinResponseDto = MyAwaitRequestJoinResponseDto.builder()
                    .joinRequestId(joinRequests.getId())
                    .postTitle(post.getTitle())
                    .build();
            myAwaitRequestJoinResponseDtoList.add(myAwaitRequestJoinResponseDto);
        }
        return myAwaitRequestJoinResponseDtoList;
    }


    // 채팅방 참가 신청 승인/거절
    @Transactional
    public String acceptJoinRequest(Long joinRequestId, boolean tOrF) {
        JoinRequests joinRequests = joinRequestsRepository.findById(joinRequestId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 신청입니다.")
        );
        Long postId = joinRequests.getPostId();
        if (!tOrF) {
            joinRequestsRepository.delete(joinRequests);
            groupService.cancelApplication2(postId,joinRequests);
            return "거절되었습니다";
        }
        User user = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );


        ChatRoom chatRoom = chatRoomRepository.findByGroup_GroupId(postId);
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
        joinRequestsRepository.delete(joinRequests);
        groupService.applyGroup2(postId,joinRequests);
        return "승인되었습니다";

        }


    // 채팅방 인원수 제한

    // allChatInfo 테이블 중복생성금지
    public Boolean checkDuplicate(User user, Long postId) {
        List<AllChatInfo> allChatInfos = allChatInfoQueryRepository.findAllByUserIdOrderByIdDesc(user.getId());
        for (AllChatInfo allChatInfo : allChatInfos) {
            if (allChatInfo.getChatRoom().getGroup().getGroupId().equals(postId)) {
                return true;
            }
        }
        return false;
    }

    // 채팅방 입장 신청 취소
    @Transactional
    public String requestJoinCancel(UserDetailsImpl userDetails, Long joinId) {
        Long userId = userDetails.getUser().getId();
        JoinRequests joinRequests = joinRequestsRepository.findByUserIdAndPostId(userId, joinId);
        joinRequestsRepository.delete(joinRequests);
        return "참가신청 취소가 완료되었습니다";
    }

    // 게시글 삭제시 승인대기 목록 삭제
    public void deleteByPostId(Long id) {
        joinRequestsRepository.deleteByPostId(id);
    }

}
