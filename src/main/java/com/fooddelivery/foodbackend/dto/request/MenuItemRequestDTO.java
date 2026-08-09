package com.fooddelivery.foodbackend.dto.request;

import com.fooddelivery.foodbackend.entity.Category;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemRequestDTO {

    @NotBlank(message = "Item name is required")
    private String itemName;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Veg/Non-Veg status is required")
    private Boolean isVeg;

    private Integer preparationTime;

}