package com.backend.chatapplicatie.payload.response;

import lombok.*;

@Data
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
