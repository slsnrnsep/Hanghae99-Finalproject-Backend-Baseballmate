package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AllChatInfoService {
    private final AllChatInfoRepository allChatInfoRepository;
    private final ChatMessageQueryRepository chatMessageQueryRepository;
    private final AllChatInfoQueryRepository allChatInfoQueryRepository;

    // 특정 채팅방의 안에 있는 유저들의 리스트를 리턴
    public List<User> getUser(Long roomId) {
        return allChatInfoQueryRepository.findAllByChatRoom_Id(roomId)
                .stream()
                .map(AllChatInfo::getEnteredUser)
                .collect(Collectors.toList());
    }

    // 채팅방 입장 시 allchatinfo에 저장
    public void saveAllChatInfo(User user, ChatRoom chatRoom) {
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
    }

    // 채팅방 나가기
    public void deleteAllChatInfo(Long roomId, UserDetailsImpl userDetails) {
        AllChatInfo allChatInfo = allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
        allChatInfoRepository.delete(allChatInfo);
    }

    // 채팅방 접속 종료시 해당 채팅방의 마지막 TALK 타입 메시지의 id를 저장
    @Transactional
    public void updateReadMessage(User user,String roomId){
        Long lastMessageId = chatMessageQueryRepository.findbyRoomIdAndTalk(roomId).getId();
        AllChatInfo allChatInfo = allChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(Long.parseLong(roomId),user.getId());
        allChatInfo.updateLastMessageId(lastMessageId);
    }
}
