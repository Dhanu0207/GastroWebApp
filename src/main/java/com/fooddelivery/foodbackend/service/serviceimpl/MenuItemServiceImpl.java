package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.MenuItemRequestDTO;
import com.fooddelivery.foodbackend.dto.response.MenuItemResponseDTO;
import com.fooddelivery.foodbackend.entity.Category;
import com.fooddelivery.foodbackend.entity.MenuItem;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CategoryRepository;
import com.fooddelivery.foodbackend.repository.MenuItemRepository;
import com.fooddelivery.foodbackend.repository.RestaurantRepository;
import com.fooddelivery.foodbackend.service.services.MenuItemService;
import com.fooddelivery.foodbackend.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadUtil fileUploadUtil;
    @Override
    public MenuItemResponseDTO createMenuItem(Long restaurantId,
                                              Long categoryId,
                                              MenuItemRequestDTO request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id : " + categoryId));

        MenuItem menuItem = MenuItem.builder()
                .itemName(request.getItemName())
                .description(request.getDescription())
                .price(request.getPrice())
                .preparationTime(request.getPreparationTime())
                .isVeg(request.getIsVeg())
                .available(true)
                .restaurant(restaurant)
                .category(category)
                .build();

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return mapToResponse(savedMenuItem);
    }

    @Override
    public List<MenuItemResponseDTO> getMenuByRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        List<MenuItem> menuItems = menuItemRepository.findByRestaurant(restaurant);

        return menuItems.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MenuItemResponseDTO> getMenuByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id : " + categoryId));

        List<MenuItem> menuItems = menuItemRepository.findByCategory(category);

        return menuItems.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(Long menuItemId,
                                              MenuItemRequestDTO request) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found with id : " + menuItemId));

        menuItem.setItemName(request.getItemName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setIsVeg(request.getIsVeg());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);

        return mapToResponse(updatedMenuItem);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found with id : " + menuItemId));

        menuItemRepository.delete(menuItem);

    }

    @Override
    public String uploadMenuItemImage(Long menuItemId,
                                      MultipartFile file)
            throws IOException {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Menu Item not found with id : "
                                        + menuItemId));

        if (menuItem.getImageUrl() != null) {
            fileUploadUtil.deleteFile(menuItem.getImageUrl());
        }

        String imageUrl = fileUploadUtil.uploadFile(file);

        menuItem.setImageUrl(imageUrl);

        menuItemRepository.save(menuItem);

        return imageUrl;
    }

    private MenuItemResponseDTO mapToResponse(MenuItem menuItem) {

        return MenuItemResponseDTO.builder()
                .menuItemId(menuItem.getMenuItemId())
                .itemName(menuItem.getItemName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .preparationTime(menuItem.getPreparationTime())
                .isVeg(menuItem.getIsVeg())
                .available(menuItem.getAvailable())
                .imageUrl(menuItem.getImageUrl())
//                .categoryId(menuItem.getCategory().getCategoryId())
//                .categoryName(menuItem.getCategory().getCategoryName())
//                .restaurantId(menuItem.getRestaurant().getRestaurantId())
//                .restaurantName(menuItem.getRestaurant().getRestaurantName())
                .build();
    }
}