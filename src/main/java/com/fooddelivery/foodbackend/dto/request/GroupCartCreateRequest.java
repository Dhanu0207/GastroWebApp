package com.fooddelivery.foodbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCartCreateRequest {

    @NotBlank(message = "Group name is required")
    private String groupName;
}
