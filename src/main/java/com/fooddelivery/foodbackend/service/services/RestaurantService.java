package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {

    RestaurantResponseDTO createRestaurant(RestaurantRequestDTO request);

    RestaurantResponseDTO getRestaurantById(Long restaurantId);

    List<RestaurantResponseDTO> getAllRestaurants();

    RestaurantResponseDTO updateRestaurant(Long restaurantId,
                                           RestaurantRequestDTO request);

    void deleteRestaurant(Long restaurantId);

}