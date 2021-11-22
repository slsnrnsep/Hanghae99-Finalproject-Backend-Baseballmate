package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;
    private final AllChatInfoService allChatInfoService;
    private final ChatRoomRepository chatRoomRepository;

    // 최초로 받은 클라이언트의 메시지를 인터셉트하여 처리
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("웹소켓에 신호 들어옴");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 웹소켓 연결 요청
        if(StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("웹소켓 연결 요청");
            String jwtToken = accessor.getFirstNativeHeader("token");
            System.out.println("토큰 확인 토큰 값:"+ jwtToken);
            System.out.println("연결 요청");
            log.info("CONNECT {}", jwtToken);
            System.out.println("토큰 유효성 검증");
            jwtTokenProvider.validateToken(jwtToken);
            System.out.println("완료");
            // 채팅방 구독 요청
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            // 헤더의 구독 destination에서 roomid 빼오기
            System.out.println("구독 요청");
            String roomId = chatMessageService.getRoomId(Optional.ofNullable((String)message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            // 채팅방을 구독한 클라이언트 sessionId를 roomId와 맵핑 -> 후에 어떤 채팅방에 어떤 세션이 있는지 알기 위함
            ChatRoom subscribedChatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
            System.out.println("해당 룸 ID:" +roomId);
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            // 클라이언트 입장 메시지를 채팅방에 발송
            // 토큰 가져오기
            String jwtToken = accessor.getFirstNativeHeader("token");
            User user;
            if (jwtToken != null) {
                user = userRepository.findByUserid(jwtTokenProvider.getUserPk(jwtToken), User.class)
                        .orElseThrow(
                                () -> new IllegalArgumentException("존재하지 않는 user입니다.")
                        );
            } else {
                throw new IllegalArgumentException("유효하지 않는 token입니다.");
            }
            // 유저 인덱스 추출
            Long userId = user.getId();
            chatRoomService.setUserEnterInfo(sessionId, roomId, userId);
            chatMessageService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .sender(user)
                    .build());
            log.info("SUBSCRIBED {}, {}", user.getUsername(), roomId);
            allChatInfoService.saveAllChatInfo(user, subscribedChatRoom);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            // 연결이 종료된 클라이언트 sessionId 로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomService.getUserEnterRoomId(sessionId);
            // 저장했던 sessionId 로 유저 객체를 받아옴
            User user = chatRoomService.chkSessionUser(sessionId);
            String username = user.getUsername();
            chatMessageService. sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.QUIT)
                    .roomId(roomId)
                    .sender(user)
                    .build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomService.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", username, roomId);
            // 유저가 퇴장할 당시의 마지막 TALK 타입 메세지 id 를 저장함
            allChatInfoService.updateReadMessage(user,roomId);
        }
        return message;
    }
}
