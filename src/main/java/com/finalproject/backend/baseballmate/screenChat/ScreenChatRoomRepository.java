package com.finalproject.backend.baseballmate.screenChat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenChatRoomRepository extends JpaRepository<ScreenChatRoom, Long> {
    ScreenChatRoom findByScreen_ScreenId(Long screenId);
}
