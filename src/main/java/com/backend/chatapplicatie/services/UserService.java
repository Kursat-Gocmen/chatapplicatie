package com.backend.chatapplicatie.services;

import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.payload.request.UpdateUserRequest;
import com.backend.chatapplicatie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public void updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        // Retrieve the user from the database
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Update the user's data using the updateUserRequest
        // This uses BeanUtils to copy properties from updateUserRequest to existingUser
        BeanUtils.copyProperties(updateUserRequest, existingUser, "id");

        // Save the updated user back to the database
        userRepository.save(existingUser);
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
