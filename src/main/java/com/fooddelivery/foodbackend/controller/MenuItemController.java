package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.MenuItemRequestDTO;
import com.fooddelivery.foodbackend.dto.response.MenuItemResponseDTO;
import com.fooddelivery.foodbackend.service.services.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;


    @PostMapping("/restaurants/{restaurantId}/categories/{categoryId}/menu-items")
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long categoryId,
            @RequestBody MenuItemRequestDTO request) {

        MenuItemResponseDTO response =
                menuItemService.createMenuItem(
                        restaurantId,
                        categoryId,
                        request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuByRestaurant(
            @PathVariable Long restaurantId) {

        List<MenuItemResponseDTO> response =
                menuItemService.getMenuByRestaurant(restaurantId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryId}/menu-items")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuByCategory(
            @PathVariable Long categoryId) {

        List<MenuItemResponseDTO> response =
                menuItemService.getMenuByCategory(categoryId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/menu-items/{menuItemId}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @PathVariable Long menuItemId,
            @RequestBody MenuItemRequestDTO request) {

        MenuItemResponseDTO response =
                menuItemService.updateMenuItem(menuItemId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long menuItemId) {

        menuItemService.deleteMenuItem(menuItemId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/menu-items/{menuItemId}/image")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long menuItemId,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        String imageUrl =
                menuItemService.uploadMenuItemImage(
                        menuItemId,
                        file);

        return ResponseEntity.ok(imageUrl);
    }
}