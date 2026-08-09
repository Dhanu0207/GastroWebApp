package com.fooddelivery.foodbackend.dto.response;

import com.fooddelivery.foodbackend.entity.enums.OrderStatus;
import com.fooddelivery.foodbackend.entity.enums.PaymentMethod;
import com.fooddelivery.foodbackend.entity.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private String orderNumber;

    private String customerName;

    private String restaurantName;

    private String deliveryAddress;

    private BigDecimal subtotal;

    private BigDecimal deliveryFee;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String orderNotes;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> orderItems;

}