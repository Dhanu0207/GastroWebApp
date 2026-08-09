package com.fooddelivery.foodbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {

    private Long cartItemId;

    private Long menuItemId;

    private String menuItemName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;
}