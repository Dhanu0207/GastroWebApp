package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.OrderRequest;
import com.fooddelivery.foodbackend.dto.response.OrderResponse;
import com.fooddelivery.foodbackend.entity.*;
import com.fooddelivery.foodbackend.entity.enums.OrderStatus;
import com.fooddelivery.foodbackend.entity.enums.PaymentStatus;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.*;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.OrderItemService;
import com.fooddelivery.foodbackend.service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemService orderItemService;
    private final SecurityUtils securityUtils;

    /** Configurable fees loaded from application.properties */
    @Value("${app.order.delivery-fee:40.00}")
    private BigDecimal deliveryFee;

    @Value("${app.order.tax-amount:25.00}")
    private BigDecimal taxAmount;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        User user = securityUtils.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart", "userId", user.getUserId()));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // ── Validate all cart items belong to the same restaurant ─────────────
        Set<Long> restaurantIds = cart.getCartItems().stream()
                .map(item -> item.getMenuItem().getRestaurant().getRestaurantId())
                .collect(Collectors.toSet());

        if (restaurantIds.size() > 1) {
            throw new BadRequestException(
                    "All cart items must belong to the same restaurant. " +
                    "Please clear the cart and add items from one restaurant only.");
        }

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address", "addressId", orderRequest.getAddressId()));

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(cart.getCartItems().get(0).getMenuItem().getRestaurant());
        order.setDeliveryAddress(address);
        order.setOrderNumber(generateOrderNumber());
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryFee(deliveryFee);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(cart.getTotalPrice().add(deliveryFee).add(taxAmount));
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderNotes(orderRequest.getOrderNotes());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setMenuItem(cartItem.getMenuItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setSubtotal(
                            cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    return orderItem;
                })
                .toList();

        orderItemRepository.saveAll(orderItems);

        // Clear cart after order placed
        cart.getCartItems().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        OrderResponse response = modelMapper.map(savedOrder, OrderResponse.class);
        response.setOrderItems(orderItemService.getOrderItems(savedOrder.getOrderId()));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        OrderResponse response = modelMapper.map(order, OrderResponse.class);
        response.setOrderItems(orderItemService.getOrderItems(orderId));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {

        User user = securityUtils.getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> {
            OrderResponse response = modelMapper.map(order, OrderResponse.class);
            response.setOrderItems(orderItemService.getOrderItems(order.getOrderId()));
            return response;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRestaurantOrders(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant", "restaurantId", restaurantId));

        List<Order> orders = orderRepository.findByRestaurant(restaurant);

        return orders.stream().map(order -> {
            OrderResponse response = modelMapper.map(order, OrderResponse.class);
            response.setOrderItems(orderItemService.getOrderItems(order.getOrderId()));
            return response;
        }).toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String orderStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid order status: " + orderStatus);
        }

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        if (order.getOrderStatus() == OrderStatus.PREPARING
                || order.getOrderStatus() == OrderStatus.READY_FOR_PICKUP
                || order.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY
                || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order cannot be cancelled at this stage.");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
}