package com.fooddelivery.foodbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDTO {

    private Long restaurantId;

    private String restaurantName;

    private String email;

    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String imageUrl;

    private String description;

    private Boolean isOpen;

    private Boolean isApproved;

    private Double rating;

}