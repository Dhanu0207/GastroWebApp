package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.response.OrderResponse;
import com.fooddelivery.foodbackend.dto.response.RestaurantResponseDTO;
import com.fooddelivery.foodbackend.dto.response.UserResponse;
import com.fooddelivery.foodbackend.entity.Order;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.OrderRepository;
import com.fooddelivery.foodbackend.repository.RestaurantRepository;
import com.fooddelivery.foodbackend.repository.UserRepository;
import com.fooddelivery.foodbackend.service.services.AdminService;
import com.fooddelivery.foodbackend.service.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;

    // ─── User Management ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setEnabled(true);
        userRepository.save(user);
    }

    // ─── Restaurant Approval ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getPendingRestaurants() {
        return restaurantRepository.findByIsApprovedFalse().stream()
                .map(this::mapRestaurant)
                .toList();
    }

    @Override
    public RestaurantResponseDTO approveRestaurant(Long restaurantId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        restaurant.setIsApproved(true);
        restaurant.setIsOpen(true);
        return mapRestaurant(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantResponseDTO rejectRestaurant(Long restaurantId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        restaurant.setIsApproved(false);
        restaurant.setIsOpen(false);
        return mapRestaurant(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantResponseDTO toggleRestaurantOpen(Long restaurantId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        restaurant.setIsOpen(!restaurant.getIsOpen());
        return mapRestaurant(restaurantRepository.save(restaurant));
    }

    // ─── Order Oversight ──────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        return orderRepository.findAll(pageable).map(order -> {
            OrderResponse response = modelMapper.map(order, OrderResponse.class);
            response.setOrderItems(orderItemService.getOrderItems(order.getOrderId()));
            return response;
        });
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Restaurant getRestaurantOrThrow(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id: " + restaurantId));
    }

    private RestaurantResponseDTO mapRestaurant(Restaurant r) {
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
