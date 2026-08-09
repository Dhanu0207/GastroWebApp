package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_OrderId(Long orderId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
