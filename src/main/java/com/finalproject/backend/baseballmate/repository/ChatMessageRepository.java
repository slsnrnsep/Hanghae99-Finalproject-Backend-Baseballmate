package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
