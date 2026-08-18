package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.LoginRequest;
import com.fooddelivery.foodbackend.dto.response.LoginResponse;
import com.fooddelivery.foodbackend.dto.request.RegisterRequest;
import com.fooddelivery.foodbackend.dto.response.UserResponse;
import com.fooddelivery.foodbackend.service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Register a new customer account. */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    /**
     * Register as a restaurant owner.
     * Account is assigned ROLE_RESTAURANT_OWNER. The owner can then create
     * a restaurant (which requires admin approval before going live).
     */
    @PostMapping("/register-restaurant-owner")
    public ResponseEntity<UserResponse> registerRestaurantOwner(
            @Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(
                authService.registerRestaurantOwner(request),
                HttpStatus.CREATED);
    }

    /** Login with email and password — returns a JWT Bearer token. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /** Get the currently authenticated user's profile. */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
