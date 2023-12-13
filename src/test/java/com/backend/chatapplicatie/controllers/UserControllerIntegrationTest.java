package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.ChatapplicatieApplication;
import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.repository.UserRepository;
import com.backend.chatapplicatie.services.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ChatapplicatieApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @AfterEach
    public void cleanup() {
        // Clean up the user table
        userRepository.deleteAll();
    }
    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("Ferhat", "Ferhat Gocmen", "Ferhat@example.com", "password123");
        User user2 = new User("Onur", "Onur Dem", "Onur@example.com", "password456");

        List<User> mockUsers = Arrays.asList(user1, user2);

        given(userService.getAllUsers()).willReturn(mockUsers);

        mockMvc.perform(get("/usermanagement/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("Ferhat"))
                .andExpect(jsonPath("$[1].username").value("Onur"));
    }


    @Test
    public void testDeleteUser() throws Exception {
        Mockito.when(userService.userExists(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/usermanagement/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Gebruiker succesvol verwijderd"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        Mockito.when(userService.userExists(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/usermanagement/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Geen gebruiker gevonden"));
    }
}
