package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.PaymentVerifyRequest;
import com.fooddelivery.foodbackend.dto.response.PaymentResponse;
import com.fooddelivery.foodbackend.entity.Order;
import com.fooddelivery.foodbackend.entity.Payment;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.entity.enums.PaymentMethod;
import com.fooddelivery.foodbackend.entity.enums.PaymentStatus;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.OrderRepository;
import com.fooddelivery.foodbackend.repository.PaymentRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SecurityUtils securityUtils;

    @Value("${razorpay.key.secret:your-razorpay-secret}")
    private String razorpaySecret;

    @Override
    public PaymentResponse createPaymentForOrder(Long orderId) {

        User user = securityUtils.getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You can only pay for your own orders");
        }

        if (order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            throw new BadRequestException("Cash on Delivery orders do not need online payment");
        }

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException("Order is already paid");
        }

        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseGet(() -> {
                    Payment newPayment = Payment.builder()
                            .order(order)
                            .razorpayOrderId("order_" + System.currentTimeMillis())
                            .amount(order.getTotalAmount())
                            .currency("INR")
                            .paymentStatus(PaymentStatus.PENDING)
                            .build();
                    return paymentRepository.save(newPayment);
                });

        return toResponse(payment);
    }

    @Override
    public PaymentResponse verifyPayment(PaymentVerifyRequest request) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + request.getRazorpayOrderId()));

        User user = securityUtils.getCurrentUser();
        Order order = payment.getOrder();

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You can only verify your own payments");
        }

        if (!isValidRazorpaySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature())) {

            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);

            throw new BadRequestException("Invalid payment signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setPaymentStatus(PaymentStatus.PAID);

        order.setPaymentStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        User user = securityUtils.getCurrentUser();

        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for order id: " + orderId));

        if (!payment.getOrder().getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You can only view your own payments");
        }

        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }


    private boolean isValidRazorpaySignature(
            String orderId,
            String paymentId,
            String signature) {

        try {
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    razorpaySecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = HexFormat.of().formatHex(hash);

            return expectedSignature.equalsIgnoreCase(signature);
        } catch (Exception ex) {
            return false;
        }
    }
}
