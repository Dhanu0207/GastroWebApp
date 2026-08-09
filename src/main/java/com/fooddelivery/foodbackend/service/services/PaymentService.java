package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.PaymentVerifyRequest;
import com.fooddelivery.foodbackend.dto.response.PaymentResponse;

public interface PaymentService {


    PaymentResponse createPaymentForOrder(Long orderId);

    PaymentResponse verifyPayment(PaymentVerifyRequest request);


    PaymentResponse getPaymentByOrderId(Long orderId);
}
