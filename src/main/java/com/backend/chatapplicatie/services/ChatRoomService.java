package com.backend.chatapplicatie.services;

import com.backend.chatapplicatie.models.ChatRoom;
import com.backend.chatapplicatie.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<ChatRoom> getChatRoom(Long senderId, Long recipientId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByUser1IdAndUser2Id(senderId, recipientId);

        if (!chatRoom.isPresent()) {
            // If the chat room is not found, try finding it in reverse order
            chatRoom = chatRoomRepository.findByUser1IdAndUser2Id(recipientId, senderId);
        }

        return chatRoom;
    }
    public Long createChatRoom(Long user1Id, Long user2Id) {
        // Your logic to create a chat room

        // Assuming you have a ChatRoom entity, set its properties
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser1Id(user1Id);
        chatRoom.setUser2Id(user2Id);

        // Save the chat room to get the id
        chatRoomRepository.save(chatRoom);

        // Now, chatRoom should have the id
        return chatRoom.getId();
    }
}
