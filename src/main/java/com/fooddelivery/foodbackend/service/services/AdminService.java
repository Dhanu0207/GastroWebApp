package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.response.OrderResponse;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {

    // ─── User Management ─────────────────────────────────────────────────────
    Page<UserResponse> getAllUsers(int page, int size);
    UserResponse getUserById(Long userId);
    void disableUser(Long userId);
    void enableUser(Long userId);

    // ─── Restaurant Approval ──────────────────────────────────────────────────
    List<RestaurantResponseDTO> getPendingRestaurants();
    RestaurantResponseDTO approveRestaurant(Long restaurantId);
    RestaurantResponseDTO rejectRestaurant(Long restaurantId);
    RestaurantResponseDTO toggleRestaurantOpen(Long restaurantId);

    // ─── Order Oversight ──────────────────────────────────────────────────────
    Page<OrderResponse> getAllOrders(int page, int size);
}
