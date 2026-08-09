package com.fooddelivery.foodbackend.dto.response;

import com.fooddelivery.foodbackend.entity.enums.AddressType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {

    private Long addressId;

    private String fullName;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String city;

    private String state;

    private String postalCode;

    private AddressType addressType;

    private Boolean isDefault;
}