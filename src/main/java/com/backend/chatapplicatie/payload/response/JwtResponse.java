package com.backend.chatapplicatie.payload.response;

import lombok.*;
@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String role;

    public JwtResponse(String accessToken, Long id, String username, String fullname, String email, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
    }
}
