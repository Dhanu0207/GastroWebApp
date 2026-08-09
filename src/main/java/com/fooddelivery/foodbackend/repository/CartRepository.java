package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Cart;
import com.fooddelivery.foodbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserUserId(Long userId);

}