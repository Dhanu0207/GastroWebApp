package com.fooddelivery.foodbackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponseDTO {

    private Long menuItemId;

    private String itemName;

    private String description;

    private Long categoryId;

    private String categoryName;

    private Long restaurantId;

    private String restaurantName;

    private BigDecimal price;

    private Boolean available;

    private Boolean isVeg;

    private Integer preparationTime;

    private String imageUrl;

}