package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Order;
import com.fooddelivery.foodbackend.entity.Restaurant;
import com.fooddelivery.foodbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUser(User user);

    List<Order> findByRestaurant(Restaurant restaurant);

}