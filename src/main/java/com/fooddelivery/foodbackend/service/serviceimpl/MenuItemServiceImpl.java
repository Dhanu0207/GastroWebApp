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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadUtil fileUploadUtil;

    // ─── Write operations ────────────────────────────────────────────────────────

    @Override
    @Transactional
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
    @Transactional
    public MenuItemResponseDTO updateMenuItem(Long menuItemId,
                                              MenuItemRequestDTO request) {

        // Use JOIN FETCH to avoid lazy-load hits during mapToResponse
        MenuItem menuItem = menuItemRepository.findByIdWithDetails(menuItemId)
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
    @Transactional
    public void deleteMenuItem(Long menuItemId) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found with id : " + menuItemId));

        menuItemRepository.delete(menuItem);
    }

    @Override
    @Transactional
    public String uploadMenuItemImage(Long menuItemId,
                                      MultipartFile file)
            throws IOException {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu Item not found with id : " + menuItemId));

        if (menuItem.getImageUrl() != null) {
            fileUploadUtil.deleteFile(menuItem.getImageUrl());
        }

        String imageUrl = fileUploadUtil.uploadFile(file);
        menuItem.setImageUrl(imageUrl);
        menuItemRepository.save(menuItem);

        return imageUrl;
    }

    // ─── Optimized read operations ───────────────────────────────────────────────

    /**
     * Previously: 2 DB round-trips + N×2 lazy-load queries.
     * Now: 1 DB round-trip with JOIN FETCH — O(1) query count regardless of result size.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuByRestaurant(Long restaurantId) {

        // Validate restaurant exists
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Restaurant not found with id : " + restaurantId);
        }

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Previously: 2 DB round-trips + N×2 lazy-load queries.
     * Now: 1 DB round-trip with JOIN FETCH.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuByCategory(Long categoryId) {

        // Validate category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(
                    "Category not found with id : " + categoryId);
        }

        return menuItemRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Search menu items by keyword (case-insensitive) within a restaurant,
     * or globally when restaurantId is null. Results are paginated and sorted
     * by item name.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> searchMenuItems(String keyword,
                                                     Long restaurantId,
                                                     int page,
                                                     int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("itemName").ascending());

        if (restaurantId != null) {
            if (!restaurantRepository.existsById(restaurantId)) {
                throw new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId);
            }
            return menuItemRepository
                    .searchByKeyword(restaurantId, keyword, pageable)
                    .map(this::mapToResponse);
        }

        return menuItemRepository
                .searchGlobal(keyword, pageable)
                .map(this::mapToResponse);
    }

    /**
     * Filtered menu fetch scoped to a restaurant. Any null filter parameter
     * is ignored (i.e., treated as "no filter").
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> getMenuByRestaurantWithFilters(Long restaurantId,
                                                                    Boolean available,
                                                                    Boolean isVeg,
                                                                    BigDecimal maxPrice,
                                                                    int page,
                                                                    int size) {

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Restaurant not found with id : " + restaurantId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());

        return menuItemRepository
                .findByRestaurantIdWithFilters(restaurantId, available, isVeg, maxPrice, pageable)
                .map(this::mapToResponse);
    }

    // ─── Mapping ─────────────────────────────────────────────────────────────────

    /**
     * All callers now use JOIN FETCH queries, so getCategory() and getRestaurant()
     * are already loaded — no lazy-load triggered here.
     */
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
                .categoryId(menuItem.getCategory().getCategoryId())
                .categoryName(menuItem.getCategory().getCategoryName())
                .restaurantId(menuItem.getRestaurant().getRestaurantId())
                .restaurantName(menuItem.getRestaurant().getRestaurantName())
                .build();
    }
}