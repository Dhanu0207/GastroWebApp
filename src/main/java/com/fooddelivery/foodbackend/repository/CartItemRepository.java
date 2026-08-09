package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.CartItem;
import com.fooddelivery.foodbackend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndMenuItem(Cart cart,
                                             MenuItem menuItem);

}