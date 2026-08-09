package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.response.OrderItemResponse;
import com.fooddelivery.foodbackend.entity.Order;
import com.fooddelivery.foodbackend.entity.OrderItem;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.OrderItemRepository;
import com.fooddelivery.foodbackend.repository.OrderRepository;
import com.fooddelivery.foodbackend.service.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderItemResponse> getOrderItems(Long orderId) {

        // STEP 1 : Validate Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order",
                                "orderId",
                                orderId));

        // STEP 2 : Fetch Order Items
        List<OrderItem> orderItems =
                orderItemRepository.findByOrder(order);

        // STEP 3 : Convert Entity to DTO
        return orderItems.stream()
                .map(this::mapToOrderItemResponse)
                .toList();
    }

    /*
     * Helper Method
     */
    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {

        OrderItemResponse response =
                modelMapper.map(orderItem, OrderItemResponse.class);

        response.setMenuItemId(
                orderItem.getMenuItem().getMenuItemId());

        response.setItemName(
                orderItem.getMenuItem().getItemName());

        response.setImageUrl(
                orderItem.getMenuItem().getImageUrl());

        return response;
    }

}