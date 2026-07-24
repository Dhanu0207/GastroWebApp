package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.CategoryRequestDTO;
import com.fooddelivery.foodbackend.dto.response.CategoryResponseDTO;
import com.fooddelivery.foodbackend.entity.Category;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CategoryRepository;
import com.fooddelivery.foodbackend.repository.RestaurantRepository;
import com.fooddelivery.foodbackend.service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public CategoryResponseDTO createCategory(Long restaurantId,
                                              CategoryRequestDTO request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .restaurant(restaurant)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDTO.builder()
                .categoryId(savedCategory.getCategoryId())
                .categoryName(savedCategory.getCategoryName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
    public List<CategoryResponseDTO> getCategoriesByRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        List<Category> categories = categoryRepository.findByRestaurant(restaurant);

        return categories.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponseDTO updateCategory(Long categoryId,
                                              CategoryRequestDTO request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id : " + categoryId));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id : " + categoryId));

        categoryRepository.delete(category);

    }


    private CategoryResponseDTO mapToResponse(Category category) {

        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();


    }
}