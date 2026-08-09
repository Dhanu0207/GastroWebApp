package com.fooddelivery.foodbackend.dto.request;

import com.fooddelivery.foodbackend.entity.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    private String landmark;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotNull
    private AddressType addressType;

    private Boolean isDefault;
}