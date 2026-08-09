package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.OrderRequest;
import com.fooddelivery.foodbackend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getMyOrders();

    List<OrderResponse> getRestaurantOrders(Long restaurantId);

    OrderResponse updateOrderStatus(Long orderId, String orderStatus);

    void cancelOrder(Long orderId);

}