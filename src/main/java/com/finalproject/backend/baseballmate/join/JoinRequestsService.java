package com.finalproject.backend.baseballmate.join;

import com.finalproject.backend.baseballmate.groupChat.*;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.AlarmRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.AlarmService;
import com.finalproject.backend.baseballmate.service.GroupService;
import com.finalproject.backend.baseballmate.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinRequestsService {

    private final GroupRepository groupRepository;
    private final ScreenRepository screenRepository;
    private final JoinRequestsRepository joinRequestsRepository;
    private final UserRepository userRepository;
    private final AllChatInfoRepository allChatInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AllChatInfoQueryRepository allChatInfoQueryRepository;
    private final AlarmService alarmService;
    private final GroupService groupService;
    private final ScreenService screenService;
    private final JoinRequestQueryRepository joinRequestQueryRepository;

    //유저 신청정보 저장
    public String requestJoin(UserDetailsImpl userDetails, Long postId) {
        Long userId = userDetails.getUser().getId();
        // 신청하려는 방과 자신의 아이디가 이미 JoinRequests DB에 있는지 확인
        if (joinRequestsRepository.findByUserIdAndPostId(userId, postId) == null) {
            Group group = groupRepository.findByGroupId(postId);
            User user = group.getCreatedUser();
            Long ownUserId = user.getId();
            JoinRequests joinRequests = new JoinRequests(userId, postId, ownUserId,"group");
            if(group.getChatRoom()==null)
            {
                joinRequestsRepository.save(joinRequests);
                AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
                String signupAlarm = user.getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 모임 : " +group.getTitle()+" 에 *참여신청을 하셨습니다.";
                alarmRequestDto.setUserId(user.getId());
                alarmRequestDto.setContents(signupAlarm);
                alarmRequestDto.setJoinRequestId(joinRequests.getId());
                alarmRequestDto.setAlarmType("Group");
                alarmService.createAlarm(alarmRequestDto);
                return "신청완료";
            }
            else
            {
                Long roomId = group.getChatRoom().getId();
                // 신청하려는 방과 자신의 아이디가 이미 AllChatInfo DB에 있는지 확인
                if (allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userId) == null) {
                    joinRequestsRepository.save(joinRequests);
                    AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
                    String signupAlarm = user.getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 모임 : " +group.getTitle()+" 에 *참여신청을 하셨습니다.";
                    alarmRequestDto.setUserId(user.getId());
                    alarmRequestDto.setContents(signupAlarm);
                    alarmRequestDto.setJoinRequestId(joinRequests.getId());
                    alarmRequestDto.setAlarmType("Group");
                    alarmService.createAlarm(alarmRequestDto);
                    return "신청완료";
                } else {
                    return "이미 속해있는 모임입니다";
                }
            }
        } else {
            return "이미 신청한 모임입니다";
        }
    }

    //유저 신청정보 불러오기
    public List<UserInfoAndPostResponseDto> requestJoinList(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JoinRequests> joinRequestsList = joinRequestQueryRepository.findtypebyUserId(userId,"group");
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
            return "거절되었습니다";
        }
        User user = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );


        ChatRoom chatRoom = chatRoomRepository.findByGroupGroupId(postId);
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
        User user = userDetails.getUser();

        JoinRequests joinRequests = joinRequestsRepository.findByUserIdAndPostId(user.getId(), joinId);
        if(joinRequests==null)
        {
            Group group =groupRepository.findByGroupId(joinId);
            groupService.cancelApplication(group.getGroupId(),userDetails);
            return "참가했던 모임에서의 취소가 완료되었습니다";
        }

        else{
            Group group=groupRepository.findByGroupId(joinRequests.getId());
            joinRequestsRepository.delete(joinRequests);
            AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
            String signupAlarm = group.getCreatedUser().getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 모임 : " +group.getTitle()+" 에 했던 *신청을 취소 하셨습니다.";
            alarmRequestDto.setUserId(group.getCreatedUser().getId());
            alarmRequestDto.setContents(signupAlarm);
            alarmRequestDto.setJoinRequestId(joinRequests.getId());
            alarmRequestDto.setAlarmType("Normal");
            alarmService.createAlarm(alarmRequestDto);

            return "참가신청 취소가 완료되었습니다";
        }


    }

    // 게시글 삭제시 승인대기 목록 삭제
    public void deleteByPostId(Long id) {
        joinRequestsRepository.deleteByPostId(id);
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    //유저 신청정보 저장
    public String requestJoin2(UserDetailsImpl userDetails, Long postId) {
        Long userId = userDetails.getUser().getId();
        // 신청하려는 방과 자신의 아이디가 이미 JoinRequests DB에 있는지 확인
        if (joinRequestsRepository.findByUserIdAndPostId(userId, postId) == null) {
            Screen group = screenRepository.findByScreenId(postId);
            User user = group.getScreenCreatedUser();
            Long ownUserId = user.getId();
            JoinRequests joinRequests = new JoinRequests(userId, postId, ownUserId,"screen");
            if(group.getScreenChatRoom()==null)
            {
                joinRequestsRepository.save(joinRequests);
                AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
                String signupAlarm = user.getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 스크린 야구모임 : " +group.getTitle()+" 에 *참여신청을 하셨습니다.";
                alarmRequestDto.setUserId(user.getId());
                alarmRequestDto.setContents(signupAlarm);
                alarmRequestDto.setJoinRequestId(joinRequests.getId());
                alarmRequestDto.setAlarmType("Screen");
                alarmService.createAlarm(alarmRequestDto);
                return "신청완료";
            }
            else
            {
                Long roomId = group.getScreenChatRoom().getId();
                // 신청하려는 방과 자신의 아이디가 이미 AllChatInfo DB에 있는지 확인
                if (allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userId) == null) {
                    joinRequestsRepository.save(joinRequests);
                    AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
                    String signupAlarm = user.getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 스크린 야구모임 : " +group.getTitle()+" 에 *참여신청을 하셨습니다.";
                    alarmRequestDto.setUserId(user.getId());
                    alarmRequestDto.setContents(signupAlarm);
                    alarmRequestDto.setJoinRequestId(joinRequests.getId());
                    alarmRequestDto.setAlarmType("Screen");
                    alarmService.createAlarm(alarmRequestDto);
                    return "신청완료";
                } else {
                    return "이미 속해있는 모임입니다";
                }
            }
        } else {
            return "이미 신청한 모임입니다";
        }
    }

    //유저 신청정보 불러오기
    public List<UserInfoAndPostResponseDto> requestJoinList2(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JoinRequests> joinRequestsList = joinRequestQueryRepository.findtypebyUserId(userId,"screen");
        List<UserInfoAndPostResponseDto> userInfoAndPostResponseDtoList = new ArrayList<>();

        for (JoinRequests joinRequests : joinRequestsList) {

            User userInfoMapping = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                    ()->  new IllegalArgumentException("회원이 아닙니다.")
            );

            Screen post = screenRepository.findByScreenId(joinRequests.getPostId());

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
    public List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinList2(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JoinRequests> joinRequestsList = joinRequestsRepository.findByUserId(userId);
        List<MyAwaitRequestJoinResponseDto> myAwaitRequestJoinResponseDtoList = new ArrayList<>();
        for (JoinRequests joinRequests : joinRequestsList) {
            Screen post = screenRepository.getById(joinRequests.getPostId());
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
    public String acceptJoinRequest2(Long joinRequestId, boolean tOrF) {
        JoinRequests joinRequests = joinRequestsRepository.findById(joinRequestId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 신청입니다.")
        );
        Long postId = joinRequests.getPostId();
        if (!tOrF) {
            joinRequestsRepository.delete(joinRequests);
            return "거절되었습니다";
        }
        User user = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );


        ChatRoom chatRoom = chatRoomRepository.findByScreenScreenId(postId);
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
        joinRequestsRepository.delete(joinRequests);
        screenService.applyScreen2(postId,joinRequests);
        return "승인되었습니다";

    }


    // 채팅방 입장 신청 취소
    @Transactional
    public String requestJoinCancel2(UserDetailsImpl userDetails, Long joinId) {
        User user = userDetails.getUser();

        JoinRequests joinRequests = joinRequestsRepository.findByUserIdAndPostId(user.getId(), joinId);
        if(joinRequests==null)
        {
            Screen group =screenRepository.findByScreenId(joinId);
            screenService.cancleApplication(group.getScreenId(),userDetails);
            return "참가했던 모임에서의 취소가 완료되었습니다";
        }

        else{
            Screen group=screenRepository.findByScreenId(joinRequests.getId());
            joinRequestsRepository.delete(joinRequests);
            AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
            String signupAlarm = group.getScreenCreatedUser().getUsername() + "님! "+userDetails.getUser().getUsername()+" 님께서* 내가 만든 모임 : " +group.getTitle()+" 에 했던 *신청을 취소 하셨습니다.";
            alarmRequestDto.setUserId(group.getScreenCreatedUser().getId());
            alarmRequestDto.setContents(signupAlarm);
            alarmRequestDto.setJoinRequestId(joinRequests.getId());
            alarmRequestDto.setAlarmType("Normal");
            alarmService.createAlarm(alarmRequestDto);

            return "참가신청 취소가 완료되었습니다";
        }


    }

}
