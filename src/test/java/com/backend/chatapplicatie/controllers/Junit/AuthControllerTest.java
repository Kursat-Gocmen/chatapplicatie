package com.backend.chatapplicatie.controllers.Junit;

import com.backend.chatapplicatie.controllers.AuthController;
import com.backend.chatapplicatie.payload.request.LoginRequest;
import com.backend.chatapplicatie.payload.response.JwtResponse;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.security.jwt.JwtUtils;
import com.backend.chatapplicatie.services.UserDetailsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    public void testAuthenticateUserSuccess() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        Authentication authentication = mock(Authentication.class);

        // Mocking authenticationManager behavior
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        // Mocking UserDetails
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John Doe", "john@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mocking jwtUtils behavior
        when(jwtUtils.generateJwtToken(any()))
                .thenReturn("mocked-jwt-token");

        // Mocking SecurityContextHolder behavior
        SecurityContextHolder.getContext().setAuthentication(null);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("mocked-jwt-token", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testuser", jwtResponse.getUsername());
        assertEquals("John Doe", jwtResponse.getFullname());
        assertEquals("john@example.com", jwtResponse.getEmail());
        assertEquals("ROLE_USER", jwtResponse.getRole());
    }

    @Test
    public void testAuthenticateUserBadCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        // Mocking authenticationManager behavior for bad credentials
        when(authenticationManager.authenticate(any()))
                .thenThrow(BadCredentialsException.class);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof MessageResponse);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Gebruikersnaam en/of wachtwoord is onjuist!", messageResponse.getMessage());
    }
}
