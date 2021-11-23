//package com.finalproject.backend.baseballmate.screenChat;
//
//import com.finalproject.backend.baseballmate.model.User;
//import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class AllScreenChatInfoService {
//    private final AllScreenChatInfoRepository allScreenChatInfoRepository;
//    private final ScreenChatMessageQueryRepository screenChatMessageQueryRepository;
//    private final AllScreenChatInfoQueryRepository allScreenChatInfoQueryRepository;
//
//    // 특정 채팅방의 안에 있는 유저들의 리스트를 리턴
//    public List<User> getUser(Long roomId) {
//        return allScreenChatInfoQueryRepository.findAllByChatRoom_Id(roomId)
//                .stream()
//                .map(AllScreenChatInfo::getEnteredUser)
//                .collect(Collectors.toList());
//    }
//
//    // 채팅방 입장 시 allchatinfo에 저장
//    public void saveAllScreenChatInfo(User user, ScreenChatRoom screenChatRoom) {
//        AllScreenChatInfo allScreenChatInfo = new AllScreenChatInfo(user, screenChatRoom);
//        allScreenChatInfoRepository.save(allScreenChatInfo);
//    }
//
//    // 채팅방 나가기
//    public void deleteAllScreenChatInfo(Long roomId, UserDetailsImpl userDetails) {
//        AllScreenChatInfo allScreenChatInfo = allScreenChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(roomId, userDetails.getUser().getId());
//        allScreenChatInfoRepository.delete(allScreenChatInfo);
//    }
//
//    // 채팅방 접속 종료시 해당 채팅방의 마지막 TALK 타입 메시지의 id를 저장
//    @Transactional
//    public void updateReadMessage(User user,String roomId){
//        Long lastMessageId = screenChatMessageQueryRepository.findByRoomIdAndTalk(roomId).getId();
//        AllScreenChatInfo allScreenChatInfo = allScreenChatInfoQueryRepository.findByChatRoom_IdAndUser_Id(Long.parseLong(roomId),user.getId());
//        allScreenChatInfo.updateLastMessageId(lastMessageId);
//    }
//}
