package com.backend.chatapplicatie.repository;

import com.backend.chatapplicatie.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    boolean existsByUser1IdAndUser2IdOrUser1IdAndUser2Id(Long user1Id, Long user2Id1, Long user2Id2, Long user1Id2);
}


