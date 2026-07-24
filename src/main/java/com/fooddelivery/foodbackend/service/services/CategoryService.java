package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.CategoryRequestDTO;
import com.fooddelivery.foodbackend.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(Long restaurantId,
                                       CategoryRequestDTO request);

    List<CategoryResponseDTO> getCategoriesByRestaurant(Long restaurantId);

    CategoryResponseDTO updateCategory(Long categoryId,
                                       CategoryRequestDTO request);

    void deleteCategory(Long categoryId);

}