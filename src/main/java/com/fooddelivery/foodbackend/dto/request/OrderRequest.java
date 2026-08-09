package com.fooddelivery.foodbackend.dto.request;

import com.fooddelivery.foodbackend.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotNull(message = "Address Id is required")
    private Long addressId;

    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;

    private String orderNotes;
}