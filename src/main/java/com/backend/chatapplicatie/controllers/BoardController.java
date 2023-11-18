package com.backend.chatapplicatie.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Tag(name = "Authorization")
@RequestMapping("/api/board")
public class BoardController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Het werkt alleen via Postman, waarbij je in het Authorization-gedeelte de optie Bearer selecteert en vervolgens het JWT invoert.")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Het werkt alleen via Postman, waarbij je in het Authorization-gedeelte de optie Bearer selecteert en vervolgens het JWT invoert.")
    public String adminAccess() {
        return "Admin Board.";
    }
}
