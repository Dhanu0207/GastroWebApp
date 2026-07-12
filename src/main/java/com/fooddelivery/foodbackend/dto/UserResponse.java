package com.fooddelivery.foodbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
