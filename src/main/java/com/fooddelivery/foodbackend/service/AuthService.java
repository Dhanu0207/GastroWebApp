package com.fooddelivery.foodbackend.service;

import com.fooddelivery.foodbackend.dto.LoginRequest;
import com.fooddelivery.foodbackend.dto.LoginResponse;
import com.fooddelivery.foodbackend.dto.RegisterRequest;
import com.fooddelivery.foodbackend.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    UserResponse getCurrentUser();
}
