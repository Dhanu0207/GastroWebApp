package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.PaymentVerifyRequest;
import com.fooddelivery.foodbackend.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPaymentForOrder(Long orderId);

    PaymentResponse verifyPayment(PaymentVerifyRequest request);

    PaymentResponse getPaymentByOrderId(Long orderId);

    /**
     * Initiate a refund via Razorpay for a cancelled/failed order.
     *
     * @param orderId the ID of the order to refund
     * @return updated PaymentResponse with REFUNDED status and refundAmount set
     */
    PaymentResponse refundPayment(Long orderId);
}
