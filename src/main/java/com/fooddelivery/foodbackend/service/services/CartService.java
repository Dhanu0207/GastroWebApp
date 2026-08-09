package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.response.CartResponse;

public interface CartService {

    CartResponse getCart();

    void clearCart();

}