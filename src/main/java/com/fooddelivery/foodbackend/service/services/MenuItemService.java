package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.MenuItemRequestDTO;
import com.fooddelivery.foodbackend.dto.response.MenuItemResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuItemService {

    MenuItemResponseDTO createMenuItem(Long restaurantId,
                                       Long categoryId,
                                       MenuItemRequestDTO request);

    List<MenuItemResponseDTO> getMenuByRestaurant(Long restaurantId);

    List<MenuItemResponseDTO> getMenuByCategory(Long categoryId);

    MenuItemResponseDTO updateMenuItem(Long menuItemId,
                                       MenuItemRequestDTO request);

    void deleteMenuItem(Long menuItemId);
    String uploadMenuItemImage(Long menuItemId, MultipartFile file) throws IOException;

}