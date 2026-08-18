package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.LoginRequest;
import com.fooddelivery.foodbackend.dto.response.LoginResponse;
import com.fooddelivery.foodbackend.dto.request.RegisterRequest;
import com.fooddelivery.foodbackend.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    UserResponse registerRestaurantOwner(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    UserResponse getCurrentUser();
}
