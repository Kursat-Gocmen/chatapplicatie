package com.backend.chatapplicatie.payload.response;

import lombok.*;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String fullname, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.roles = roles;
    }
}
