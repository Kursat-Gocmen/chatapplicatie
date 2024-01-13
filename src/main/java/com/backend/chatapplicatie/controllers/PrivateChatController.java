package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.ChatRoom;
import com.backend.chatapplicatie.models.PrivateMessage;
import com.backend.chatapplicatie.services.ChatRoomService;
import com.backend.chatapplicatie.services.PrivateMessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/private-chat")
@RequiredArgsConstructor
public class PrivateChatController {

    private final PrivateMessageService privateMessageService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/messages/{chatRoomId}")
    @Operation(summary = "Get private messages for a chat room", description = "Retrieve a list of private messages for a specific chat room.")
    public ResponseEntity<List<PrivateMessage>> getPrivateMessages(@PathVariable Long chatRoomId) {
        List<PrivateMessage> privateMessages = privateMessageService.getPrivateMessagesByChatRoom(chatRoomId);
        return ResponseEntity.ok(privateMessages);

    }

    @MessageMapping("/private-chat")
    @SendTo("/topic/private-message")
    public PrivateMessage sendPrivateMessage(@Payload PrivateMessage privateMessage) {
        privateMessage.setTimestamp(new Date());

        if (privateMessage.getChatRoom() == null) {
            // If chatRoom is null, set it based on senderId and receiverId
            Optional<ChatRoom> chatRoomOptional = chatRoomService.getChatRoom(privateMessage.getSenderId(), privateMessage.getReceiverId());

            if (chatRoomOptional.isPresent()) {
                ChatRoom chatRoom = chatRoomOptional.get();
                // Set chatRoom in the privateMessage
                privateMessage.setChatRoom(chatRoom);

                // Set receiverId based on user2Id in the chatRoom
                Long receiverId = (privateMessage.getSenderId().equals(chatRoom.getUser1Id()))
                        ? chatRoom.getUser2Id()
                        : chatRoom.getUser1Id();
                privateMessage.setReceiverId(receiverId);
            } else {
                throw new RuntimeException("Chat room not found for senderId: " + privateMessage.getSenderId() + " and recipientId: " + privateMessage.getReceiverId());
            }
        }

        privateMessageService.saveMessage(privateMessage);

        return privateMessage;
    }

    @PostMapping("/create-room/{user1Id}/{user2Id}")
    @Operation(summary = "Create a private chat room", description = "Create a private chat room between two users.")
    public ResponseEntity<?> createPrivateChatRoom(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        try {
            // Check if a chat room already exists between the two users or in reverse order
            Optional<ChatRoom> existingChatRoom = chatRoomService.getChatRoom(user1Id, user2Id);
            if (!existingChatRoom.isPresent()) {
                existingChatRoom = chatRoomService.getChatRoom(user2Id, user1Id);
            }

            // If a chat room already exists, return its ID
            if (existingChatRoom.isPresent()) {
                Map<String, Object> existingRoomResponse = new HashMap<>();
                existingRoomResponse.put("message", "Chat room already exists");
                existingRoomResponse.put("chatRoomId", existingChatRoom.get().getId());
                return ResponseEntity.ok(existingRoomResponse);
            }

            // Create the chat room if it doesn't exist
            Long createdChatRoomId = chatRoomService.createChatRoom(user1Id, user2Id);

            // Return the chat room information in the response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Chat room created successfully");
            response.put("chatRoomId", createdChatRoomId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating chat room");
        }
    }
}

