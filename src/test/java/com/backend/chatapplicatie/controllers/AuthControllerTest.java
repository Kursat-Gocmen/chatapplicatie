package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.payload.request.LoginRequest;
import com.backend.chatapplicatie.payload.response.JwtResponse;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.repository.UserRepository;
import com.backend.chatapplicatie.security.jwt.JwtUtils;
import com.backend.chatapplicatie.security.services.UserDetailsImpl;
import com.backend.chatapplicatie.security.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class AuthControllerTest {

    //CONTROLLER LAAG GETEST
    @InjectMocks
    AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    public void login_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "password");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "testUser",
                "TestUser",
                "test@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock the authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock the behavior of jwtUtils.generateJwtToken
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockedJWT");

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jwtResponse.getToken()).isEqualTo("mockedJWT");
        assertThat(jwtResponse.getId()).isEqualTo(userDetails.getId());
        assertThat(jwtResponse.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(jwtResponse.getFullname()).isEqualTo(userDetails.getFullname());
        assertThat(jwtResponse.getEmail()).isEqualTo(userDetails.getEmail());
        assertThat(jwtResponse.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    public void login_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "incorrectPassword");

        // Mock the behavior of authenticationManager.authenticate to throw BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("Gebruikersnaam en/of wachtwoord is onjuist!");
    }


    //SERVICE LAAG GETEST.
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void getAllUsers_Success() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User(1L, "testuser1", "testuser1@test.nl"),
                new User(2L, "testuser2", "testuser2@test.nl")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser1");
        assertThat(result.get(0).getEmail()).isEqualTo("testuser1@test.nl");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getUsername()).isEqualTo("testuser2");
        assertThat(result.get(1).getEmail()).isEqualTo("testuser2@test.nl");
    }

    @Test
    public void deleteUser_Success() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void userExists_True() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = userService.userExists(userId);

        // Assert
        assertThat(result).isTrue();
    }
    @Test
    public void userExists_False() {
        // Arrange
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean result = userService.userExists(userId);

        // Assert
        assertThat(result).isFalse();
    }
}
