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

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
            ){
        UserResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        LoginResponse response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(){
        UserResponse userResponse = authService.getCurrentUser();

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
