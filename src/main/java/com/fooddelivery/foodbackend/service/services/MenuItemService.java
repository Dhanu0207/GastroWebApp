package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.MenuItemRequestDTO;
import com.fooddelivery.foodbackend.dto.response.MenuItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface MenuItemService {

    MenuItemResponseDTO createMenuItem(Long restaurantId,
                                       Long categoryId,
                                       MenuItemRequestDTO request);

    /** Get all items for a restaurant (optimized, single-query JOIN FETCH). */
    List<MenuItemResponseDTO> getMenuByRestaurant(Long restaurantId);

    /** Get all items for a category (optimized, single-query JOIN FETCH). */
    List<MenuItemResponseDTO> getMenuByCategory(Long categoryId);

    MenuItemResponseDTO updateMenuItem(Long menuItemId,
                                       MenuItemRequestDTO request);

    void deleteMenuItem(Long menuItemId);

    String uploadMenuItemImage(Long menuItemId, MultipartFile file) throws IOException;

    /**
     * Keyword search scoped to a restaurant, paginated.
     *
     * @param keyword    search term matched against itemName and description (case-insensitive)
     * @param restaurantId scope search to this restaurant; pass {@code null} for global search
     * @param page       zero-based page index
     * @param size       page size
     */
    Page<MenuItemResponseDTO> searchMenuItems(String keyword,
                                              Long restaurantId,
                                              int page,
                                              int size);

    /**
     * Filtered menu fetch for a restaurant, paginated.
     *
     * @param restaurantId target restaurant
     * @param available    filter by availability; {@code null} = no filter
     * @param isVeg        filter by veg/non-veg; {@code null} = no filter
     * @param maxPrice     upper price bound; {@code null} = no filter
     * @param page         zero-based page index
     * @param size         page size
     */
    Page<MenuItemResponseDTO> getMenuByRestaurantWithFilters(Long restaurantId,
                                                             Boolean available,
                                                             Boolean isVeg,
                                                             BigDecimal maxPrice,
                                                             int page,
                                                             int size);
}