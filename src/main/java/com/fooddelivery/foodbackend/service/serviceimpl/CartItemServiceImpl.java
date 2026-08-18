package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.response.CartItemResponse;
import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.CartItem;
import com.fooddelivery.foodbackend.entity.MenuItem;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.CartItemRepository;
import com.fooddelivery.foodbackend.repository.CartRepository;
import com.fooddelivery.foodbackend.repository.MenuItemRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final SecurityUtils securityUtils;   // replaces direct SecurityContextHolder usage

    @Override
    public CartItemResponse addItem(CartItemRequest request) {

        User user = securityUtils.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalItems(0)
                            .totalPrice(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu Item not found with id: " + request.getMenuItemId()));

        if (menuItem.getAvailable() != null && !menuItem.getAvailable()) {
            throw new BadRequestException("Menu item '" + menuItem.getItemName() + "' is currently unavailable.");
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndMenuItem(cart, menuItem)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .price(menuItem.getPrice())
                    .subtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .build();
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        updateCartTotals(cart);

        return mapToResponse(savedCartItem, menuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems() {

        User user = securityUtils.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cartItemRepository.findByCart(cart).stream()
                .map(item -> mapToResponse(item, item.getMenuItem()))
                .toList();
    }

    @Override
    public CartItemResponse updateQuantity(Long cartItemId, CartItemRequest request) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart Item not found with id : " + cartItemId));

        cartItem.setQuantity(request.getQuantity());
        cartItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        updateCartTotals(updatedCartItem.getCart());

        return mapToResponse(updatedCartItem, updatedCartItem.getMenuItem());
    }

    @Override
    public void removeItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart Item not found with id : " + cartItemId));

        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);
        updateCartTotals(cart);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private void updateCartTotals(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
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

    private CartItemResponse mapToResponse(CartItem cartItem, MenuItem menuItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .menuItemId(menuItem.getMenuItemId())
                .menuItemName(menuItem.getItemName())
                .imageUrl(menuItem.getImageUrl())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .subtotal(cartItem.getSubtotal())
                .build();
    }
}