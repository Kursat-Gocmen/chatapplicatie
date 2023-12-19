package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.ChatMessage;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.services.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Public Chat")
public class ChatController {

    private final ChatMessageService chatMessageService;
    @GetMapping("/messages")
    @Operation(summary = "Alle berichten ophalen", description = "Haal een lijst op van alle berichten.")
    public ResponseEntity<List<ChatMessage>> getPublicMessages() {
        List<ChatMessage> chatMessages = chatMessageService.getPublicMessages();
        return ResponseEntity.ok(chatMessages);
    }

    @DeleteMapping("/clean")
    @Operation(summary = "Alle berichten verwijderen", description = "Verwijder alle berichten die er zijn in de public chat.")
    public ResponseEntity<MessageResponse> cleanChatMessages() {
        chatMessageService.cleanChatMessages();
        return ResponseEntity.ok(new MessageResponse("Chat messages cleaned successfully"));
    }
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(new Date());

        chatMessageService.saveMessage(chatMessage);

        return chatMessage;
    }
}