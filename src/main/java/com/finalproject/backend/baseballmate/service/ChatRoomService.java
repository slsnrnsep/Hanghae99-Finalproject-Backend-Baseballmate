package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.responseDto.ChatRoomCreateResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ChatRoomResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final AllChatInfoQueryRepository allChatInfoQueryRepository;

    public static final String ENTER_INFO = "ENTER_INFO"; //채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    public static final String USER_INFO = "USER_INFO"; //채팅방에 입장한 클라이언트 수 저장

    // HashOperations 레디스에서 쓰는 자료형
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsUserInfo;
    private final ChatRoomRepository chatRoomRepository;
    private final AllChatInfoRepository allChatInfoRepository;
    private final UserRepository userRepository;
    private final ChatMessageQueryRepository chatMessageQueryRepository;
    private final GroupRepository groupRepository;
    private final ScreenRepository screenRepository;


    //채팅방생성
    @Transactional
    public ChatRoomCreateResponseDto createChatRoom(Long groupId, User user) {
        String uuid = UUID.randomUUID().toString();
        Group group = groupRepository.findByGroupId(groupId);
        ChatRoom chatRoom = new ChatRoom(uuid, group, user);
        chatRoomRepository.save(chatRoom);
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
        ChatRoomCreateResponseDto chatRoomCreateResponseDto = new ChatRoomCreateResponseDto(chatRoom, group);
        return chatRoomCreateResponseDto;
    }

    //채팅방생성
    @Transactional
    public ChatRoomCreateResponseDto createChatRoom2(Long groupId, User user) {
        String uuid = UUID.randomUUID().toString();
        Screen group = screenRepository.findByScreenId(groupId);
        ChatRoom chatRoom = new ChatRoom(uuid, group, user);
        chatRoomRepository.save(chatRoom);
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
        ChatRoomCreateResponseDto chatRoomCreateResponseDto = new ChatRoomCreateResponseDto(chatRoom, group);
        return chatRoomCreateResponseDto;
    }

    public boolean newMessage(UserDetailsImpl userDetails) {
        List<ChatRoomResponseDto> chatRoomResponseDtoList = getOnesChatRoom(userDetails.getUser());
        for (ChatRoomResponseDto chatRoomListResponseDto : chatRoomResponseDtoList) {
            if (chatRoomListResponseDto.isNewMessage()){
                return true;
            }
        }
        return false;
    }

    // 사용자별 채팅방 목록 조회
    public List<ChatRoomResponseDto> getOnesChatRoom(User user) {
        List<ChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<AllChatInfo> allChatInfoList = allChatInfoQueryRepository.findAllByUserIdOrderByIdDesc(user.getId());
        for (AllChatInfo allChatInfo : allChatInfoList) {
            ChatRoom chatRoom = allChatInfo.getChatRoom();
            if(chatRoom.getGroup()==null)
            {
                Screen screen = chatRoom.getScreen();
                Long headCountChat = allChatInfoQueryRepository.countAllByChatRoom(chatRoom);
                String chatRoomId = Long.toString(chatRoom.getId());
                Long myLastMessageId = allChatInfo.getLastMessageId();
                ChatMessage newLastMessage = chatMessageQueryRepository.findbyRoomIdAndTalk(chatRoomId);
                Long newLastMessageId = 0L;
                if (newLastMessage != null) {
                    newLastMessageId = newLastMessage.getId();
                }

                // myLastMessageId 와 newLastMessageId 를 비교하여 현재 채팅방에 새 메세지가 있는지 여부를 함께 내려줌
                ChatRoomResponseDto responseDto;
                if (myLastMessageId < newLastMessageId) {
                    responseDto = new ChatRoomResponseDto(chatRoom, screen, headCountChat, true);
                } else {
                    responseDto = new ChatRoomResponseDto(chatRoom, screen, headCountChat, false);
                }
                responseDtoList.add(responseDto);
            }
            if(chatRoom.getScreen()==null)
            {
                Group group = chatRoom.getGroup();
                Long headCountChat = allChatInfoQueryRepository.countAllByChatRoom(chatRoom);
                String chatRoomId = Long.toString(chatRoom.getId());
                Long myLastMessageId = allChatInfo.getLastMessageId();
                ChatMessage newLastMessage = chatMessageQueryRepository.findbyRoomIdAndTalk(chatRoomId);
                Long newLastMessageId = 0L;
                if (newLastMessage != null) {
                    newLastMessageId = newLastMessage.getId();
                }

                // myLastMessageId 와 newLastMessageId 를 비교하여 현재 채팅방에 새 메세지가 있는지 여부를 함께 내려줌
                ChatRoomResponseDto responseDto;
                if (myLastMessageId < newLastMessageId) {
                    responseDto = new ChatRoomResponseDto(chatRoom, group, headCountChat, true);
                } else {
                    responseDto = new ChatRoomResponseDto(chatRoom, group, headCountChat, false);
                }
                responseDtoList.add(responseDto);
            }
        }
        return responseDtoList;
    }

    // redis 에 입장정보로 sessionId 와 roomId를 저장하고 해당 sessionId 와 토큰에서 받아온 userId를 저장
    public void setUserEnterInfo(String sessionId, String roomId, Long userId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
        hashOpsUserInfo.put(USER_INFO, sessionId, Long.toString(userId));
    }

    // redis 에 저장했던 sessionId 로 roomId를 리턴
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저가 나갈때 redis 에 저장했던 해당 세션 / 유저의 정보를 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
        hashOpsUserInfo.delete(USER_INFO, sessionId);
    }

    // redis 에 저장했던 sessionId 로 userId 를 얻어오고 해당 userId 로 User 객체를 찾아 리턴
    public User chkSessionUser(String sessionId) {
        Long userId = Long.parseLong(Objects.requireNonNull(hashOpsUserInfo.get(USER_INFO, sessionId)));
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자"));
    }

    // 게시글 삭제시 채팅방도 삭제
    @Transactional
    public void deleteAllChatInfo(Long roomId, UserDetailsImpl userDetails) {
        AllChatInfo allChatInfo = allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
        if (allChatInfo != null) {
            allChatInfoRepository.delete(allChatInfo);
        }
    }

    // 채팅방 나가기
    @Transactional
    public void quitChat(Long groupId, UserDetailsImpl userDetails) {
        Group group = groupRepository.findByGroupId(groupId);
        if (group == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Long roomId = group.getChatRoom().getId();
        if (isChatRoomOwner(group, userDetails)) {
            // 확정된 모임이고 글쓴이면 게시글, 채팅방 아예 삭제 -> 채팅방 남겨둘지 말지 결정해서 정하기
            deleteAllChatInfo(roomId, userDetails);
        } else {
            // 일반 유저일 때 채팅방 나가기
            AllChatInfo allChatInfo = allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
            allChatInfoRepository.delete(allChatInfo);
        }
    }

    // 채팅방 나가기
    @Transactional
    public void quitChat2(Long groupId, UserDetailsImpl userDetails) {
        Screen group = screenRepository.findByScreenId(groupId);
        if (group == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Long roomId = group.getScreenChatRoom().getId();
        if (isChatRoomOwner2(group, userDetails)) {
            // 확정된 모임이고 글쓴이면 게시글, 채팅방 아예 삭제 -> 채팅방 남겨둘지 말지 결정해서 정하기
            deleteAllChatInfo(roomId, userDetails);
        } else {
            // 일반 유저일 때 채팅방 나가기
            AllChatInfo allChatInfo = allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
            allChatInfoRepository.delete(allChatInfo);
        }
    }


    // 채팅방 주인 확인
    static boolean isChatRoomOwner(Group group, UserDetailsImpl userDetails) {
        Long roomOwnerId = group.getChatRoom().getOwnUserId();
        Long userId = userDetails.getUser().getId();
        return roomOwnerId.equals(userId);
    }

    // 채팅방 주인 확인
    static boolean isChatRoomOwner2(Screen group, UserDetailsImpl userDetails) {
        Long roomOwnerId = group.getScreenChatRoom().getOwnUserId();
        Long userId = userDetails.getUser().getId();
        return roomOwnerId.equals(userId);
    }

    // 채팅방 chatValid -> false
    @Transactional
    public void updateChatValid(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(""));
        chatRoom.updatechatValid(false);
    }

    public void setGroupNull(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(""));
        chatRoom.setGroup(null);
    }

}
