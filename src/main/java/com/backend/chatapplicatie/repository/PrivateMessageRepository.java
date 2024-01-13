package com.backend.chatapplicatie.repository;

import com.backend.chatapplicatie.models.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findByChatRoomId(Long chatRoomId);
}

