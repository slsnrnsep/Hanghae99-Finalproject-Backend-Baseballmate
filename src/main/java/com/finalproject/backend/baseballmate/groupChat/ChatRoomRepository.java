package com.finalproject.backend.baseballmate.groupChat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByGroup_GroupId(Long groupId);
//    ChatRoom findById(Long chatRoomId);

    ChatRoom findByScreen_ScreenId(Long screenId);
}
