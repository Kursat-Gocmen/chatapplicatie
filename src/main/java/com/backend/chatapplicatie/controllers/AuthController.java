package com.backend.chatapplicatie.controllers;

import com.backend.chatapplicatie.models.ERole;
import com.backend.chatapplicatie.models.Role;
import com.backend.chatapplicatie.models.User;
import com.backend.chatapplicatie.payload.request.LoginRequest;
import com.backend.chatapplicatie.payload.request.SignupRequest;
import com.backend.chatapplicatie.payload.response.JwtResponse;
import com.backend.chatapplicatie.payload.response.MessageResponse;
import com.backend.chatapplicatie.repository.RoleRepository;
import com.backend.chatapplicatie.repository.UserRepository;
import com.backend.chatapplicatie.security.jwt.JwtUtils;
import com.backend.chatapplicatie.security.services.UserDetailsImpl;
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
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Gebruikersnaam en/of wachtwoord is onjuist!"));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
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

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_USER' not found."));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Gebruiker succesvol geregistreerd!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(new MessageResponse("Logout successvol"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error during logout"));
        }
    }
}
