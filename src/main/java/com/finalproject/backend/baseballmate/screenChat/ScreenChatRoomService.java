package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
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
public class ScreenChatRoomService {

    private final AllScreenChatInfoQueryRepository allScreenChatInfoQueryRepository;

    public static final String SCREEN_ENTER_INFO = "SCREEN_ENTER_INFO"; //채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    public static final String SCREEN_USER_INFO = "SCREEN_USER_INFO"; //채팅방에 입장한 클라이언트 수 저장

    // HashOperations 레디스에서 쓰는 자료형
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsScreenEnterInfo;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsScreenUserInfo;
    private final ScreenChatRoomRepository screenChatRoomRepository;
    private final AllScreenChatInfoRepository allScreenChatInfoRepository;
    private final UserRepository userRepository;
    private final ScreenChatMessageQueryRepository screenChatMessageQueryRepository;
    private final ScreenRepository screenRepository;


    //채팅방생성
    @Transactional
    public ScreenChatRoomCreateResponseDto createChatRoom(Long screenId, User user) {
        String uuid = UUID.randomUUID().toString();
        Screen screen = screenRepository.findByScreenId(screenId);
        ScreenChatRoom screenChatRoom = new ScreenChatRoom(uuid, screen, user);
        screenChatRoomRepository.save(screenChatRoom);
        ScreenChatRoomCreateResponseDto screenChatRoomCreateResponseDto = new ScreenChatRoomCreateResponseDto(screenChatRoom, screen);
        return screenChatRoomCreateResponseDto;
    }

    public boolean newMessage(UserDetailsImpl userDetails) {
        List<ScreenChatRoomResponseDto> screenChatRoomResponseDtoList = getOnesChatRoom(userDetails.getUser());
        for (ScreenChatRoomResponseDto screenChatRoomResponseDto : screenChatRoomResponseDtoList) {
            if (screenChatRoomResponseDto.isNewMessage()){
                return true;
            }
        }
        return false;
    }

    // 사용자별 채팅방 목록 조회
    public List<ScreenChatRoomResponseDto> getOnesChatRoom(User user) {
        List<ScreenChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<AllScreenChatInfo> allScreenChatInfoList = allScreenChatInfoQueryRepository.findAllByUserIdOrderByIdDesc(user.getId());
        for (AllScreenChatInfo allScreenChatInfo : allScreenChatInfoList) {
            ScreenChatRoom screenChatRoom = allScreenChatInfo.getScreenChatRoom();
            Screen screen = screenChatRoom.getScreenGroup();
            Long headCountChat = allScreenChatInfoQueryRepository.countAllByScrenChatRoom(screenChatRoom);
            String screenChatRoomId = Long.toString(screenChatRoom.getId());
            Long myLastMessageId = allScreenChatInfo.getLastMessageId();
            ScreenChatMessage newLastMessage = screenChatMessageQueryRepository.findByRoomIdAndTalk(screenChatRoomId);
            Long newLastMessageId = 0L;
            if (newLastMessage != null) {
                newLastMessageId = newLastMessage.getId();
            }

            // myLastMessageId 와 newLastMessageId 를 비교하여 현재 채팅방에 새 메세지가 있는지 여부를 함께 내려줌
            ScreenChatRoomResponseDto responseDto;
            if (myLastMessageId < newLastMessageId) {
                responseDto = new ScreenChatRoomResponseDto(screenChatRoom, screen, headCountChat, true);
            } else {
                responseDto = new ScreenChatRoomResponseDto(screenChatRoom, screen, headCountChat, false);
            }
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    // redis 에 입장정보로 sessionId 와 roomId를 저장하고 해당 sessionId 와 토큰에서 받아온 userId를 저장
    public void setUserEnterInfo(String sessionId, String roomId, Long userId) {
        hashOpsScreenEnterInfo.put(SCREEN_ENTER_INFO, sessionId, roomId);
        hashOpsScreenUserInfo.put(SCREEN_USER_INFO, sessionId, Long.toString(userId));
    }

    // redis 에 저장했던 sessionId 로 roomId를 리턴
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsScreenUserInfo.get(SCREEN_ENTER_INFO, sessionId);
    }

    // 유저가 나갈때 redis 에 저장했던 해당 세션 / 유저의 정보를 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsScreenEnterInfo.delete(SCREEN_ENTER_INFO, sessionId);
        hashOpsScreenUserInfo.delete(SCREEN_USER_INFO, sessionId);
    }

    // redis 에 저장했던 sessionId 로 userId 를 얻어오고 해당 userId 로 User 객체를 찾아 리턴
    public User chkSessionUser(String sessionId) {
        Long userId = Long.parseLong(Objects.requireNonNull(hashOpsScreenUserInfo.get(SCREEN_USER_INFO, sessionId)));
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자"));
    }

    // 게시글 삭제시 채팅방도 삭제
    @Transactional
    public void deleteAllChatInfo(Long roomId, UserDetailsImpl userDetails) {
        AllScreenChatInfo allScreenChatInfo = allScreenChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
        if (allScreenChatInfo != null) {
            allScreenChatInfoRepository.delete(allScreenChatInfo);
        }
    }

    // 채팅방 나가기
    @Transactional
    public void quitChat(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Long roomId = screen.getScreenChatRoom().getId();
        if (isScreenChatRoomOwner(screen, userDetails)) {
            // 확정된 모임이고 글쓴이면 게시글, 채팅방 아예 삭제 -> 채팅방 남겨둘지 말지 결정해서 정하기
            deleteAllChatInfo(roomId, userDetails);
        } else {
            // 일반 유저일 때 채팅방 나가기

            AllScreenChatInfo allScreenChatInfo = allScreenChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
            allScreenChatInfoRepository.delete(allScreenChatInfo);
        }
    }

    // 채팅방 주인 확인
    static boolean isScreenChatRoomOwner(Screen screen, UserDetailsImpl userDetails) {
        Long roomOwnerId = screen.getScreenChatRoom().getOwnUserId();
        Long userId = userDetails.getUser().getId();
        return roomOwnerId.equals(userId);
    }

    // 채팅방 chatValid -> false
    @Transactional
    public void updateChatValid(Long roomId) {
        ScreenChatRoom screenChatRoom = screenChatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(""));
        screenChatRoom.updatechatValid(false);
    }
}
