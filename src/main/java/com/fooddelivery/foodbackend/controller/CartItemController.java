package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.response.CartItemResponse;
import com.fooddelivery.foodbackend.service.services.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart/items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItemResponse> addItem(
            @Valid @RequestBody CartItemRequest request) {

        return new ResponseEntity<>(
                cartItemService.addItem(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems() {

        return ResponseEntity.ok(
                cartItemService.getCartItems()
        );
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemRequest request) {

        return ResponseEntity.ok(
                cartItemService.updateQuantity(cartItemId, request)
        );
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long cartItemId) {

        cartItemService.removeItem(cartItemId);

        return ResponseEntity.noContent().build();
    }
}