package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.PaymentVerifyRequest;
import com.fooddelivery.foodbackend.dto.response.PaymentResponse;
import com.fooddelivery.foodbackend.service.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.createPaymentForOrder(orderId));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(orderId));
    }
}
