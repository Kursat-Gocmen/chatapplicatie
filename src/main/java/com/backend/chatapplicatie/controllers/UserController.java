package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.payload.request.UpdateUserRequest;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/usermanagement")
@Tag(name = "CRUD")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @Operation(summary = "Alle gebruikers ophalen", description = "Haal een lijst op van alle gebruikers.")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Gebruiker verwijderen op basis van ID", description = "Verwijder een gebruiker op basis van hun ID.")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        if (userService.userExists(id)) { userService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse("Gebruiker succesvol verwijderd"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Geen gebruiker gevonden"));
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Gebruiker bijwerken op basis van ID", description = "Werk de gegevens van een gebruiker bij op basis van hun ID.")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        if (userService.userExists(id)) {
            userService.updateUser(id, updateUserRequest);
            return ResponseEntity.ok(new MessageResponse("Gebruiker succesvol bijgewerkt"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Geen gebruiker gevonden"));
        }
    }
}
