package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.response.CartItemResponse;

import java.util.List;

public interface CartItemService {

    CartItemResponse addItem(CartItemRequest request);

    List<CartItemResponse> getCartItems();

    CartItemResponse updateQuantity(Long cartItemId,
                                    CartItemRequest request);

    void removeItem(Long cartItemId);

}