package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Order;
import com.fooddelivery.foodbackend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

}