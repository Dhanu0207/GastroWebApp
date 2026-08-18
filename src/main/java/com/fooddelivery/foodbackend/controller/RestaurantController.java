package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.service.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // ─── Create ──────────────────────────────────────────────────────────────

    /**
     * Restaurant owners register their restaurant.
     * Newly created restaurants are isApproved=false until an admin approves.
     */
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantRequestDTO request) {

        return new ResponseEntity<>(
                restaurantService.createRestaurant(request),
                HttpStatus.CREATED);
    }

    // ─── Read ─────────────────────────────────────────────────────────────────

    /**
     * Public listing of approved & open restaurants.
     * Example: GET /api/restaurants?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDTO>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(restaurantService.getAllRestaurants(page, size));
    }

    /**
     * Keyword search across restaurant name and city.
     * Example: GET /api/restaurants/search?keyword=pizza&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantResponseDTO>> searchRestaurants(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(restaurantService.searchRestaurants(keyword, page, size));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody RestaurantRequestDTO request) {   // added @Valid

        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId, request));
    }

    // ─── Image Upload ─────────────────────────────────────────────────────────

    /**
     * Upload or replace the restaurant banner image.
     * Example: POST /api/restaurants/1/image (multipart/form-data, field name "file")
     */
    @PostMapping("/{restaurantId}/image")
    public ResponseEntity<RestaurantResponseDTO> uploadRestaurantImage(
            @PathVariable Long restaurantId,
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(
                restaurantService.uploadRestaurantImage(restaurantId, file));
    }

    // ─── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long restaurantId) {

        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}