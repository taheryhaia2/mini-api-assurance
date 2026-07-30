package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.domain.User;
import com.assurance.mini_api_assurance.dto.AuthResponse;
import com.assurance.mini_api_assurance.dto.LoginRequest;
import com.assurance.mini_api_assurance.dto.RegisterRequest;
import com.assurance.mini_api_assurance.security.CustomUserDetails;
import com.assurance.mini_api_assurance.security.JwtService;
import com.assurance.mini_api_assurance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.assurance.mini_api_assurance.domain.Role;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(
                request.username(),
                request.password(),
                request.role(),
                request.firstName(),
                request.lastName(),
                request.email()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("User created: " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsernameForAuth(request.username());
        String token = jwtService.generateToken(userDetails);

        // On récupère le username et le rôle
        String username = userDetails.getUsername();
        Role role = userDetails.getUser().getRole();

        return ResponseEntity.ok(new AuthResponse(token, username, role));
    }
}
