package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.RestaurantRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.RestaurantService;
import com.fooddelivery.foodbackend.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SecurityUtils securityUtils;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO request) {

        if (restaurantRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Restaurant email already exists.");
        }

        Restaurant restaurant = Restaurant.builder()
                .restaurantName(request.getRestaurantName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .description(request.getDescription())
                .owner(securityUtils.getCurrentUser())
                .isApproved(false)   // requires admin approval
                .isOpen(false)
                .build();

        return mapToResponse(restaurantRepository.save(restaurant));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getRestaurantById(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        return mapToResponse(restaurant);
    }

    /**
     * Public listing — only approved & open restaurants, paginated, sorted by name.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponseDTO> getAllRestaurants(int page, int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("restaurantName").ascending());
        return restaurantRepository
                .findByIsApprovedTrueAndIsOpenTrue(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponseDTO> searchRestaurants(String keyword, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("restaurantName").ascending());
        return restaurantRepository
                .searchApprovedRestaurants(keyword, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public RestaurantResponseDTO updateRestaurant(Long restaurantId,
                                                  RestaurantRequestDTO request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        if (!restaurant.getEmail().equals(request.getEmail())
                && restaurantRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Restaurant email already exists.");
        }

        restaurant.setRestaurantName(request.getRestaurantName());
        restaurant.setEmail(request.getEmail());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setState(request.getState());
        restaurant.setPincode(request.getPincode());
        restaurant.setDescription(request.getDescription());

        return mapToResponse(restaurantRepository.save(restaurant));
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        restaurantRepository.delete(restaurant);
    }

    @Override
    public RestaurantResponseDTO uploadRestaurantImage(Long restaurantId,
                                                       MultipartFile file)
            throws IOException {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        if (restaurant.getImageUrl() != null) {
            fileUploadUtil.deleteFile(restaurant.getImageUrl());
        }

        restaurant.setImageUrl(fileUploadUtil.uploadFile(file));
        return mapToResponse(restaurantRepository.save(restaurant));
    }

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private RestaurantResponseDTO mapToResponse(Restaurant r) {
        return RestaurantResponseDTO.builder()
                .restaurantId(r.getRestaurantId())
                .restaurantName(r.getRestaurantName())
                .email(r.getEmail())
                .phoneNumber(r.getPhoneNumber())
                .address(r.getAddress())
                .city(r.getCity())
                .state(r.getState())
                .pincode(r.getPincode())
                .description(r.getDescription())
                .imageUrl(r.getImageUrl())
                .isOpen(r.getIsOpen())
                .isApproved(r.getIsApproved())
                .rating(r.getRating())
                .build();
    }
}