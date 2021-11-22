package com.finalproject.backend.baseballmate.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    // 클라이언트에서 메세지가 도착하면 해당 메세지를 messagingTemplate 으로 컨버팅하고 다른 구독자들에게 전송한뒤 해당 메세지를 DB에 저장함
    public void sendMessage(String publishMessage) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            messagingTemplate.convertAndSend("/sub/api/chat/rooms/" + chatMessage.getRoomId(), chatMessage);
            ChatMessage message = new ChatMessage();
            message.setType(chatMessage.getType());
            message.setRoomId(chatMessage.getRoomId());
            message.setSenderId(chatMessage.getSenderId());
            message.setSenderName(chatMessage.getSenderName());
            message.setSenderImage(chatMessage.getSenderImage());
            message.setMessage(chatMessage.getMessage());
            message.setUserEmail(chatMessage.getUserEmail());
            chatMessageRepository.save(message);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
