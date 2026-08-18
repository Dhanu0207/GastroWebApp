package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.MenuItemRequestDTO;
import com.fooddelivery.foodbackend.dto.response.MenuItemResponseDTO;
import com.fooddelivery.foodbackend.service.services.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    // ─── Create ──────────────────────────────────────────────────────────────────

    @PostMapping("/restaurants/{restaurantId}/categories/{categoryId}/menu-items")
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long categoryId,
            @RequestBody MenuItemRequestDTO request) {

        MenuItemResponseDTO response =
                menuItemService.createMenuItem(restaurantId, categoryId, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ─── Read / Browse ───────────────────────────────────────────────────────────

    /** Get all menu items for a restaurant (single JOIN FETCH query). */
    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuByRestaurant(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(menuItemService.getMenuByRestaurant(restaurantId));
    }

    /** Get all menu items under a category (single JOIN FETCH query). */
    @GetMapping("/categories/{categoryId}/menu-items")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(menuItemService.getMenuByCategory(categoryId));
    }

    // ─── Search ──────────────────────────────────────────────────────────────────

    /**
     * Keyword search across itemName and description for a specific restaurant.
     *
     * <p>Example: {@code GET /api/restaurants/1/menu-items/search?keyword=pizza&page=0&size=10}
     */
    @GetMapping("/restaurants/{restaurantId}/menu-items/search")
    public ResponseEntity<Page<MenuItemResponseDTO>> searchMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                menuItemService.searchMenuItems(keyword, restaurantId, page, size));
    }

    /**
     * Global keyword search across all restaurants.
     *
     * <p>Example: {@code GET /api/menu-items/search?keyword=burger&page=0&size=10}
     */
    @GetMapping("/menu-items/search")
    public ResponseEntity<Page<MenuItemResponseDTO>> searchMenuItemsGlobal(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                menuItemService.searchMenuItems(keyword, null, page, size));
    }

    // ─── Filter ──────────────────────────────────────────────────────────────────

    /**
     * Filter menu items for a restaurant by availability, veg/non-veg, and max price.
     * All filter params are optional — omit any to skip that filter.
     *
     * <p>Example: {@code GET /api/restaurants/1/menu-items/filter?available=true&isVeg=true&maxPrice=200&page=0&size=10}
     */
    @GetMapping("/restaurants/{restaurantId}/menu-items/filter")
    public ResponseEntity<Page<MenuItemResponseDTO>> filterMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Boolean isVeg,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                menuItemService.getMenuByRestaurantWithFilters(
                        restaurantId, available, isVeg, maxPrice, page, size));
    }

    // ─── Update / Delete ─────────────────────────────────────────────────────────

    @PutMapping("/menu-items/{menuItemId}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @PathVariable Long menuItemId,
            @RequestBody MenuItemRequestDTO request) {

        return ResponseEntity.ok(menuItemService.updateMenuItem(menuItemId, request));
    }

    @DeleteMapping("/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long menuItemId) {

        menuItemService.deleteMenuItem(menuItemId);
        return ResponseEntity.noContent().build();
    }

    // ─── Image upload ─────────────────────────────────────────────────────────────

    @PostMapping("/menu-items/{menuItemId}/image")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long menuItemId,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return ResponseEntity.ok(
                menuItemService.uploadMenuItemImage(menuItemId, file));
    }
}