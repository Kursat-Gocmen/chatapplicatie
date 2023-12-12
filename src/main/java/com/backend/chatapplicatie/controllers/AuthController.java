package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.ERole;
import com.backend.chatapplicatie.models.Role;
import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.payload.request.LoginRequest;
import com.backend.chatapplicatie.payload.request.SignUpRequest;
import com.backend.chatapplicatie.payload.response.JwtResponse;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.repository.RoleRepository;
import com.backend.chatapplicatie.repository.UserRepository;
import com.backend.chatapplicatie.security.jwt.JwtUtils;
import com.backend.chatapplicatie.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    @Operation(summary = "Authenticatie van gebruiker", description = "Verifieert de inloggegevens van een gebruiker en genereert een JWT-token.")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream().findFirst().orElse(null).getAuthority();

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getFullname(),
                    userDetails.getEmail(),
                    role));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Gebruikersnaam en/of wachtwoord is onjuist!"));
        }
    }
    @Operation(summary = "Registreer een nieuwe gebruiker", description = "Registreert een nieuwe gebruiker met de opgegeven informatie.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Gebruikersnaam is al in gebruik!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is al in gebruik!"));
        }

        User user = new User(signUpRequest.getUsername(),signUpRequest.getFullname(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(ERole.USER_ROLE)
                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_USER' not found."));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Gebruiker succesvol geregistreerd!"));
    }

    @PostMapping("/logout")
    @Operation(summary = "werkt niet")
    public ResponseEntity<?> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(new MessageResponse("Logout successvol"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error during logout"));
        }
    }
}
