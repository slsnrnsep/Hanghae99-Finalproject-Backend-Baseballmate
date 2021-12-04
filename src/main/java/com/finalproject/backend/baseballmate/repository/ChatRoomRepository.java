package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ChatRoom;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByGroupGroupId(Long groupId);

    ChatRoom findByScreenScreenId(Long screenId);

    ChatRoom findByGroup(Group group);
    ChatRoom findByScreen(Screen screen);
}
