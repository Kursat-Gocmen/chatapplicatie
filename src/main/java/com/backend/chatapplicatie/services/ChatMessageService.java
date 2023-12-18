package com.backend.chatapplicatie.services;

import com.backend.chatapplicatie.models.ChatMessage;
import com.backend.chatapplicatie.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    public List<ChatMessage> getPublicMessages() {return chatMessageRepository.findAll();}

    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public void cleanChatMessages() {
        chatMessageRepository.deleteAll();}
}