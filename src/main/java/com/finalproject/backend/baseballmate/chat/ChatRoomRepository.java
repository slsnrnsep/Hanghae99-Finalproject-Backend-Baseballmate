package com.finalproject.backend.baseballmate.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByGroup_GroupId(Long groupId);
}
