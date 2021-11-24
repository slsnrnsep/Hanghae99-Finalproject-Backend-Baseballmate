package com.finalproject.backend.baseballmate.groupChat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByGroupGroupId(Long groupId);
//    ChatRoom findById(Long chatRoomId);

    ChatRoom findByScreenScreenId(Long screenId);
}
