package com.fooddelivery.foodbackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long orderItemId;

    private Long menuItemId;

    private String itemName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;

}