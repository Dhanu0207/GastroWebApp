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
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SecurityUtils securityUtils;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Override
    public PaymentResponse createPaymentForOrder(Long orderId) {

        User user = securityUtils.getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId));

        // Check whether this order belongs to the logged-in user
        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException(
                    "You can only pay for your own orders");
        }

        // COD does not require Razorpay
        if (order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            throw new BadRequestException(
                    "Cash on Delivery orders do not need online payment");
        }

        // Don't allow payment for an already paid order
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException(
                    "Order is already paid");
        }

        // Check whether we already created a Razorpay order
        Payment payment = paymentRepository
                .findByOrder_OrderId(orderId)
                .orElse(null);

        if (payment != null) {
            return toResponse(payment);
        }

        try {

            /*
             * Razorpay expects the amount in the smallest
             * currency unit.
             *
             * ₹649.50 → 64950 paise
             */
            long amountInPaise = order.getTotalAmount()
                    .movePointRight(2)
                    .longValueExact();

            JSONObject orderRequest = new JSONObject();

            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", order.getOrderNumber());

            JSONObject notes = new JSONObject();

            notes.put("orderId", order.getOrderId());
            notes.put("userId", user.getUserId());

            orderRequest.put("notes", notes);

            /*
             * ACTUAL Razorpay API call
             */
            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            /*
             * Get REAL Razorpay order ID
             */
            String razorpayOrderId =
                    razorpayOrder.get("id");

            /*
             * Save Razorpay order information
             * in our database.
             */
            payment = Payment.builder()
                    .order(order)
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);

            return toResponse(payment);

        } catch (Exception ex) {

            throw new BadRequestException(
                    "Unable to create Razorpay order: "
                            + ex.getMessage());
        }
    }

    @Override
    public PaymentResponse verifyPayment(
            PaymentVerifyRequest request) {

        // 1. Find our payment using the Razorpay order ID
        Payment payment = paymentRepository
                .findByRazorpayOrderId(
                        request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for Razorpay order id: "
                                        + request.getRazorpayOrderId()));

        // 2. Get logged-in user
        User user = securityUtils.getCurrentUser();

        Order order = payment.getOrder();

        // 3. Verify ownership
        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException(
                    "You can only verify your own payments");
        }

        // 4. Prevent duplicate verification
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return toResponse(payment);
        }

        try {

            // IMPORTANT:
            // Use the Razorpay order ID stored in OUR database.
            JSONObject verificationData = new JSONObject();

            verificationData.put(
                    "razorpay_order_id",
                    payment.getRazorpayOrderId());

            verificationData.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId());

            verificationData.put(
                    "razorpay_signature",
                    request.getRazorpaySignature());

            // 5. Verify Razorpay signature
            boolean signatureValid =
                    Utils.verifyPaymentSignature(
                            verificationData,
                            getRazorpaySecret());

            if (!signatureValid) {

                payment.setPaymentStatus(
                        PaymentStatus.FAILED);

                order.setPaymentStatus(
                        PaymentStatus.FAILED);

                paymentRepository.save(payment);
                orderRepository.save(order);

                throw new BadRequestException(
                        "Invalid Razorpay payment signature");
            }

            // 6. Fetch actual payment from Razorpay
            com.razorpay.Payment razorpayPayment =
                    razorpayClient.payments.fetch(
                            request.getRazorpayPaymentId());

            String razorpayStatus =
                    razorpayPayment.get("status");

            // 7. Payment must be captured
            if (!"captured".equalsIgnoreCase(razorpayStatus)) {

                payment.setPaymentStatus(
                        PaymentStatus.PENDING);

                paymentRepository.save(payment);

                throw new BadRequestException(
                        "Payment has not been captured yet");
            }

            // 8. Save Razorpay payment details
            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId());

            payment.setRazorpaySignature(
                    request.getRazorpaySignature());

            payment.setPaymentStatus(
                    PaymentStatus.PAID);

            // 9. Update Order
            order.setPaymentStatus(
                    PaymentStatus.PAID);

            // 10. Save both
            paymentRepository.save(payment);
            orderRepository.save(order);

            return toResponse(payment);

        } catch (RazorpayException ex) {

            throw new BadRequestException(
                    "Unable to verify Razorpay payment: "
                            + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        User user = securityUtils.getCurrentUser();

        Payment payment = paymentRepository
                .findByOrder_OrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: "
                                        + orderId));

        if (!payment.getOrder()
                .getUser()
                .getUserId()
                .equals(user.getUserId())) {

            throw new BadRequestException(
                    "You can only view your own payments");
        }

        return toResponse(payment);
    }

    @Override
    public PaymentResponse refundPayment(Long orderId) {

        User user = securityUtils.getCurrentUser();

        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for order id: " + orderId));

        if (!payment.getOrder().getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You can only refund your own payments");
        }

        if (payment.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BadRequestException(
                    "Only paid orders can be refunded. Current status: " + payment.getPaymentStatus());
        }

        try {
            // Call Razorpay refund API
            long refundAmountInPaise = payment.getAmount()
                    .movePointRight(2)
                    .longValueExact();

            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", refundAmountInPaise);
            refundRequest.put("speed", "normal");

            razorpayClient.payments.refund(
                    payment.getRazorpayPaymentId(), refundRequest);

            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            payment.setRefundAmount(payment.getAmount());

            Order order = payment.getOrder();
            order.setPaymentStatus(PaymentStatus.REFUNDED);

            paymentRepository.save(payment);
            orderRepository.save(order);

            return toResponse(payment);

        } catch (RazorpayException ex) {
            throw new BadRequestException("Unable to process refund: " + ex.getMessage());
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentStatus(payment.getPaymentStatus())
                .refundAmount(payment.getRefundAmount())
                .build();
    }

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private String getRazorpaySecret() {
        return razorpaySecret;
    }
}