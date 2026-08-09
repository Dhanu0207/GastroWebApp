package com.fooddelivery.foodbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {

    private Long cartId;

    private BigDecimal totalPrice;

    private Integer totalItems;

    private List<CartItemResponse> cartItems;

}