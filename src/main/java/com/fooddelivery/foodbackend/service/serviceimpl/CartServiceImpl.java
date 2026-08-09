package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.response.CartResponse;
import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CartRepository;
import com.fooddelivery.foodbackend.repository.UserRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

//    private final UserRepository userRepository;
private final SecurityUtils securityUtils;
    @Override
    public CartResponse getCart() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));

        User user = securityUtils.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .build();
    }
    @Override
    public void clearCart() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
        User user = securityUtils.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));

        cart.getCartItems().clear();

        cart.setTotalItems(0);

        cart.setTotalPrice(BigDecimal.ZERO);

        cartRepository.save(cart);

    }
}