package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.response.OrderResponse;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.dto.response.UserResponse;
import com.fooddelivery.foodbackend.service.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-only endpoints. All routes under /api/admin/** are restricted to
 * users with ROLE_ADMIN (enforced in SecurityConfig).
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ─── User Management ─────────────────────────────────────────────────────

    /**
     * List all registered users, paginated.
     * GET /api/admin/users?page=0&size=20
     */
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size));
    }

    /** GET /api/admin/users/{userId} */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    /** Disable a user account. PUT /api/admin/users/{userId}/disable */
    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long userId) {
        adminService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    /** Re-enable a disabled user account. PUT /api/admin/users/{userId}/enable */
    @PutMapping("/users/{userId}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable Long userId) {
        adminService.enableUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ─── Restaurant Approval ──────────────────────────────────────────────────

    /**
     * List restaurants pending admin approval.
     * GET /api/admin/restaurants/pending
     */
    @GetMapping("/restaurants/pending")
    public ResponseEntity<List<RestaurantResponseDTO>> getPendingRestaurants() {
        return ResponseEntity.ok(adminService.getPendingRestaurants());
    }

    /**
     * Approve a restaurant — sets isApproved=true and isOpen=true.
     * PUT /api/admin/restaurants/{restaurantId}/approve
     */
    @PutMapping("/restaurants/{restaurantId}/approve")
    public ResponseEntity<RestaurantResponseDTO> approveRestaurant(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminService.approveRestaurant(restaurantId));
    }

    /**
     * Reject / un-approve a restaurant.
     * PUT /api/admin/restaurants/{restaurantId}/reject
     */
    @PutMapping("/restaurants/{restaurantId}/reject")
    public ResponseEntity<RestaurantResponseDTO> rejectRestaurant(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminService.rejectRestaurant(restaurantId));
    }

    /**
     * Toggle a restaurant's open/closed status without affecting approval.
     * PUT /api/admin/restaurants/{restaurantId}/toggle-open
     */
    @PutMapping("/restaurants/{restaurantId}/toggle-open")
    public ResponseEntity<RestaurantResponseDTO> toggleRestaurantOpen(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminService.toggleRestaurantOpen(restaurantId));
    }

    // ─── Order Oversight ──────────────────────────────────────────────────────

    /**
     * View all orders across the entire platform, newest first.
     * GET /api/admin/orders?page=0&size=20
     */
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getAllOrders(page, size));
    }
}
