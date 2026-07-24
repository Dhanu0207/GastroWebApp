package com.fooddelivery.foodbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long categoryId;

    private String categoryName;

    private String description;

}