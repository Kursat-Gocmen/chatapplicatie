package com.backend.chatapplicatie.services;

import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User("username", "testuser1", "testuser1@test.nl", null),
                new User("username2", "testuser2", "testuser2@test.nl", null)
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("username");
        assertThat(result.get(0).getEmail()).isEqualTo("testuser1@test.nl");
        assertThat(result.get(0).getPassword()).isNull();
        assertThat(result.get(1).getUsername()).isEqualTo("username2");
        assertThat(result.get(1).getEmail()).isEqualTo("testuser2@test.nl");
        assertThat(result.get(1).getPassword()).isNull();
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void userExists_True() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = userService.userExists(userId);

        // Assert
        assertThat(result).isTrue();
    }
    @Test
    void userExists_False() {
        // Arrange
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean result = userService.userExists(userId);

        // Assert
        assertThat(result).isFalse();
    }
}
