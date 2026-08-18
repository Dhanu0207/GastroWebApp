package com.fooddelivery.foodbackend.dto.response;

import com.fooddelivery.foodbackend.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class PaymentResponse {

    private Long paymentId;
    private Long orderId;
    private String razorpayOrderId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus paymentStatus;
    private BigDecimal refundAmount;
}
