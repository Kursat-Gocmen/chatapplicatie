package com.backend.chatapplicatie.services;

import com.backend.chatapplicatie.models.ChatMessage;
import com.backend.chatapplicatie.models.ChatRoom;
import com.backend.chatapplicatie.models.PrivateMessage;
import com.backend.chatapplicatie.repository.PrivateMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;



    public List<PrivateMessage> getPrivateMessagesByChatRoom(Long chatRoomId) {
        return privateMessageRepository.findByChatRoomId(chatRoomId);
    }


    public void saveMessage(PrivateMessage privateMessage) {
        privateMessageRepository.save(privateMessage);
    }

}

