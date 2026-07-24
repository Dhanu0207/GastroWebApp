package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.service.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;


    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @PathVariable Long restaurantId) {

        RestaurantResponseDTO response =
                restaurantService.getRestaurantById(restaurantId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {

        List<RestaurantResponseDTO> response =
                restaurantService.getAllRestaurants();

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody RestaurantRequestDTO request) {

        RestaurantResponseDTO response =
                restaurantService.updateRestaurant(restaurantId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long restaurantId) {

        restaurantService.deleteRestaurant(restaurantId);

        return ResponseEntity.noContent().build();
    }
}