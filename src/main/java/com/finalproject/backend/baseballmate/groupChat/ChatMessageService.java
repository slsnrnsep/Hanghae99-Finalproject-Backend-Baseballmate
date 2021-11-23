package com.finalproject.backend.baseballmate.groupChat;

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
public class ChatMessageService {

    private final ChatMessageQueryRepository chatMessageQueryRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
//    private final BanUserListService banUserListService;
    private final ChatRoomService chatRoomService;
    private final ChatMessage chatMessage = new ChatMessage();


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
    public void sendChatMessage(ChatMessage chatMessageRequestDto) {
        // 채팅방 입장시
        User user = userRepository.findById(chatMessageRequestDto.getSenderId()).orElseThrow(
                ()-> new IllegalArgumentException("(채팅방) 유저인덱스를 찾을 수 없습니다")
        );
        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage("ID: "+user.getId()+"  "+user.getUsername()+ "님이 들어왔어요.");
//            chatMessageRequestDto.setMessage(chatMessageRequestDto.getSenderId() + "님이 들어왔어요.");
//            chatMessageRequestDto.setSender(chatMessageRequestDto.getSender());
//            chatMessage.setMessage(chatMessageRequestDto.getMessage());
//            chatMessage.setSender(chatMessageRequestDto.getSender());
//            chatMessage.setRoomId(chatMessageRequestDto.getRoomId());
//            chatMessage.setType(ChatMessage.MessageType.ENTER);
//            chatMessage.setUserEmail(chatMessageRequestDto.getUserEmail());
//            chatMessageRepository.save(chatMessage);
            // 채팅방 퇴장시
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage("ID: "+user.getId()+"  "+user.getUsername() + "님이 자리를 비웠어요.");
//            chatMessageRequestDto.setSender(chatMessageRequestDto.getSender());
//            chatMessage.setMessage(chatMessageRequestDto.getMessage());
//            chatMessage.setSender(chatMessageRequestDto.getSender());
//            chatMessage.setRoomId(chatMessageRequestDto.getRoomId());
//            chatMessage.setType(ChatMessage.MessageType.QUIT);
//            chatMessage.setUserEmail(chatMessageRequestDto.getUserEmail());
//            chatMessageRepository.save(chatMessage);
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
    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt" );
        pageable = PageRequest.of(page, 150, sort );
        return chatMessageQueryRepository.findByRoomIdOrderByIdDesc(roomId, pageable);
    }

}
