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

    private BigDecimal price;

    private Boolean available;

    private Boolean isVeg;

    private Integer preparationTime;

    private String imageUrl;

}