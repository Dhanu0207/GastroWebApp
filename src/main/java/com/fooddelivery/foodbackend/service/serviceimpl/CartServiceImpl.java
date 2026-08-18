package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.response.CartResponse;
import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CartRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        User user = securityUtils.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return toResponse(cart);
    }

    @Override
    public void clearCart() {
        User user = securityUtils.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getCartItems().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .build();
    }
}