package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RestaurantService {

    RestaurantResponseDTO createRestaurant(RestaurantRequestDTO request);

    RestaurantResponseDTO getRestaurantById(Long restaurantId);

    /** Public listing — approved & open restaurants, paginated. */
    Page<RestaurantResponseDTO> getAllRestaurants(int page, int size);

    /** Keyword search across name and city, approved restaurants only. */
    Page<RestaurantResponseDTO> searchRestaurants(String keyword, int page, int size);

    RestaurantResponseDTO updateRestaurant(Long restaurantId, RestaurantRequestDTO request);

    void deleteRestaurant(Long restaurantId);

    /** Upload or replace the restaurant banner image. */
    RestaurantResponseDTO uploadRestaurantImage(Long restaurantId, MultipartFile file) throws IOException;
}