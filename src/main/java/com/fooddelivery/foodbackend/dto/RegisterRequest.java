package com.fooddelivery.foodbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is Required")
    private String lastname;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone Number must Contain exactly 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "password is required")
    private String password;
}
