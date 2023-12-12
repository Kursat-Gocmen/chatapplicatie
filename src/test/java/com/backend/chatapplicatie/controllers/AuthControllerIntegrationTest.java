package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.ChatapplicatieApplication;
import com.backend.chatapplicatie.payload.request.LoginRequest;
import com.backend.chatapplicatie.payload.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest("testuser", "Test User", "testuser@example.com", "USER_ROLE", "password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Gebruiker succesvol geregistreerd!\"}", true)); // Ignore additional fields
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("testuser", "Test User", "testuser@example.com", "USER_ROLE", "password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
