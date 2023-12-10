package com.backend.chatapplicatie.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}