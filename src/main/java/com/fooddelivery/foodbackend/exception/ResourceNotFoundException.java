package com.fooddelivery.foodbackend.exception;

import jakarta.validation.constraints.NotNull;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String address, String addressId, @NotNull(message = "Address Id is required") Long addressId1) {
    }

    public ResourceNotFoundException(String user, String authentication, String notAuthenticated) {
    }
}
