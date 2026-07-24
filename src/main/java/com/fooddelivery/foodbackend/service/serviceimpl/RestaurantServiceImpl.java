package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.RestaurantRequestDTO;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.RestaurantRepository;
import com.fooddelivery.foodbackend.service.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

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
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDTO.builder()
                .restaurantId(savedRestaurant.getRestaurantId())
                .restaurantName(savedRestaurant.getRestaurantName())
                .email(savedRestaurant.getEmail())
                .phoneNumber(savedRestaurant.getPhoneNumber())
                .address(savedRestaurant.getAddress())
                .city(savedRestaurant.getCity())
                .state(savedRestaurant.getState())
                .pincode(savedRestaurant.getPincode())
                .description(savedRestaurant.getDescription())
                .imageUrl(savedRestaurant.getImageUrl())
                .isOpen(savedRestaurant.getIsOpen())
                .rating(savedRestaurant.getRating())
                .build();
    }

    @Override
    public RestaurantResponseDTO getRestaurantById(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        return RestaurantResponseDTO.builder()
                .restaurantId(restaurant.getRestaurantId())
                .restaurantName(restaurant.getRestaurantName())
                .email(restaurant.getEmail())
                .phoneNumber(restaurant.getPhoneNumber())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .state(restaurant.getState())
                .pincode(restaurant.getPincode())
                .description(restaurant.getDescription())
                .imageUrl(restaurant.getImageUrl())
                .isOpen(restaurant.getIsOpen())
                .rating(restaurant.getRating())
                .build();
    }

    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> RestaurantResponseDTO.builder()
                        .restaurantId(restaurant.getRestaurantId())
                        .restaurantName(restaurant.getRestaurantName())
                        .email(restaurant.getEmail())
                        .phoneNumber(restaurant.getPhoneNumber())
                        .address(restaurant.getAddress())
                        .city(restaurant.getCity())
                        .state(restaurant.getState())
                        .pincode(restaurant.getPincode())
                        .description(restaurant.getDescription())
                        .imageUrl(restaurant.getImageUrl())
                        .isOpen(restaurant.getIsOpen())
                        .rating(restaurant.getRating())
                        .build())
                .toList();
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

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDTO.builder()
                .restaurantId(updatedRestaurant.getRestaurantId())
                .restaurantName(updatedRestaurant.getRestaurantName())
                .email(updatedRestaurant.getEmail())
                .phoneNumber(updatedRestaurant.getPhoneNumber())
                .address(updatedRestaurant.getAddress())
                .city(updatedRestaurant.getCity())
                .state(updatedRestaurant.getState())
                .pincode(updatedRestaurant.getPincode())
                .description(updatedRestaurant.getDescription())
                .imageUrl(updatedRestaurant.getImageUrl())
                .isOpen(updatedRestaurant.getIsOpen())
                .rating(updatedRestaurant.getRating())
                .build();
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id : " + restaurantId));

        restaurantRepository.delete(restaurant);

    }
}