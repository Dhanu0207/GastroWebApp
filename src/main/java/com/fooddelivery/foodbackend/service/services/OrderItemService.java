package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.response.OrderItemResponse;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> getOrderItems(Long orderId);

}