package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.response.CartItemResponse;
import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.CartItem;
import com.fooddelivery.foodbackend.entity.MenuItem;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CartItemRepository;
import com.fooddelivery.foodbackend.repository.CartRepository;
import com.fooddelivery.foodbackend.repository.MenuItemRepository;
import com.fooddelivery.foodbackend.repository.UserRepository;
import com.fooddelivery.foodbackend.service.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    private final MenuItemRepository menuItemRepository;

    private final UserRepository userRepository;

    @Override
    public CartItemResponse addItem(CartItemRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email : " + email));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalItems(0)
                            .totalPrice(BigDecimal.ZERO)
                            .build();

                    return cartRepository.save(newCart);

                });

        MenuItem menuItem = menuItemRepository.findById(
                        request.getMenuItemId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Menu Item not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndMenuItem(cart, menuItem)
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity());

            cartItem.setSubtotal(
                    cartItem.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    ));

        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .price(menuItem.getPrice())
                    .subtotal(
                            menuItem.getPrice().multiply(
                                    BigDecimal.valueOf(request.getQuantity())
                            )
                    )
                    .build();

        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return CartItemResponse.builder()
                .cartItemId(savedCartItem.getCartItemId())
                .menuItemId(menuItem.getMenuItemId())
                .menuItemName(menuItem.getItemName())
                .imageUrl(menuItem.getImageUrl())
                .quantity(savedCartItem.getQuantity())
                .price(savedCartItem.getPrice())
                .subtotal(savedCartItem.getSubtotal())
                .build();
    }

    @Override
    public List<CartItemResponse> getCartItems() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email : " + email));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);

        return cartItems.stream()
                .map(cartItem -> CartItemResponse.builder()
                        .cartItemId(cartItem.getCartItemId())
                        .menuItemId(cartItem.getMenuItem().getMenuItemId())
                        .menuItemName(cartItem.getMenuItem().getItemName())
                        .imageUrl(cartItem.getMenuItem().getImageUrl())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .subtotal(cartItem.getSubtotal())
                        .build())
                .toList();
    }

    @Override
    public CartItemResponse updateQuantity(Long cartItemId,
                                           CartItemRequest request) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart Item not found with id : " + cartItemId));

        cartItem.setQuantity(request.getQuantity());

        cartItem.setSubtotal(
                cartItem.getPrice().multiply(
                        BigDecimal.valueOf(request.getQuantity())
                )
        );

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        updateCartTotals(updatedCartItem.getCart());

        return CartItemResponse.builder()
                .cartItemId(updatedCartItem.getCartItemId())
                .menuItemId(updatedCartItem.getMenuItem().getMenuItemId())
                .menuItemName(updatedCartItem.getMenuItem().getItemName())
                .imageUrl(updatedCartItem.getMenuItem().getImageUrl())
                .quantity(updatedCartItem.getQuantity())
                .price(updatedCartItem.getPrice())
                .subtotal(updatedCartItem.getSubtotal())
                .build();
    }

    private void updateCartTotals(Cart cart) {

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);

        int totalItems = 0;

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            totalItems += item.getQuantity();

            totalPrice = totalPrice.add(item.getSubtotal());

        }

        cart.setTotalItems(totalItems);

        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
    }

    @Override
    public void removeItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart Item not found with id : " + cartItemId));

        Cart cart = cartItem.getCart();

        cartItemRepository.delete(cartItem);

        updateCartTotals(cart);
    }
}