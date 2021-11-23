package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ScreenChatMessageService {
    private final ScreenChatMessageQueryRepository screenChatMessageQueryRepository;
    private final ScreenChatMessageRepository screenChatMessageRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    //    private final BanUserListService banUserListService;
    private final ScreenChatRoomService screenChatRoomService;
    private final ScreenChatMessage screenChatMessage = new ScreenChatMessage();


    // 메세지의 헤더에서 추출한 정보로 roomId 를 확인하고 리턴
    @Transactional
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            throw new IllegalArgumentException("lastIndex 오류입니다.");
        }
    }

    // 메세지의 type 을 확인하고 그에따라 작업을 분기시킴
    @Transactional
    public void sendScreenChatMessage(ScreenChatMessage chatMessageRequestDto) {
        // 채팅방 입장시
        User user = userRepository.findById(chatMessageRequestDto.getSenderId()).orElseThrow(
                ()-> new IllegalArgumentException("(채팅방) 유저인덱스를 찾을 수 없습니다")
        );
        if (ScreenChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage("ID: "+user.getId()+"  "+user.getUsername()+ "님이 들어왔어요.");
//            chatMessageRequestDto.setMessage(chatMessageRequestDto.getSenderId() + "님이 들어왔어요.");
//            chatMessageRequestDto.setSender(chatMessageRequestDto.getSender());
            // 채팅방 퇴장시
        } else if (ScreenChatMessage.MessageType.QUIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage("ID: "+user.getId()+"  "+user.getUsername() + "님이 자리를 비웠어요.");
//            chatMessageRequestDto.setSender(chatMessageRequestDto.getSender());
            // 채팅방 강퇴시
//        } else if (ChatMessage.MessageType.BAN.equals(chatMessageRequestDto.getType())){
//            Long userId = Long.parseLong(chatMessageRequestDto.getMessage());
//            Long roomId = Long.parseLong(chatMessageRequestDto.getRoomId());
//            banUserListService.banUser(userId,roomId);
//            // 채팅방 폭파시
//        }else if (ChatMessage.MessageType.BREAK.equals(chatMessageRequestDto.getType())) {
//            Long roomId = Long.parseLong(chatMessageRequestDto.getRoomId());
//            chatRoomService.updateChatValid(roomId);
//        }
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageRequestDto);
    }


    // 채팅방의 마지막 150개 메세지를 페이징하여 리턴함
    public Page<ScreenChatMessage> getScreenChatMessageByRoomId(String roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, 150, sort );
        return screenChatMessageQueryRepository.findByRoomIdOrderByIdDesc(roomId, pageable);
    }
}
