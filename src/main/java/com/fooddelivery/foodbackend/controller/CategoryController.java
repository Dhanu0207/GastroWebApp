package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.CategoryRequestDTO;
import com.fooddelivery.foodbackend.dto.response.CategoryResponseDTO;
import com.fooddelivery.foodbackend.service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @PathVariable Long restaurantId,
            @RequestBody CategoryRequestDTO request) {

        CategoryResponseDTO response =
                categoryService.createCategory(restaurantId, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByRestaurant(
            @PathVariable Long restaurantId) {

        List<CategoryResponseDTO> response =
                categoryService.getCategoriesByRestaurant(restaurantId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequestDTO request) {

        CategoryResponseDTO response =
                categoryService.updateCategory(categoryId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }

}