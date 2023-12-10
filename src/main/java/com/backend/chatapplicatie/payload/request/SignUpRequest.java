package com.backend.chatapplicatie.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String fullname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String role;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;

    public SignUpRequest(String username, String fullname, String email, String role, String password) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
        this.password = password;
    }
}