package com.fooddelivery.foodbackend.dto.response;

import com.fooddelivery.foodbackend.entity.enums.GroupCartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCartResponse {

    private Long groupCartId;
    private String groupName;
    private String inviteCode;
    private UserResponse hostUser;
    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private GroupCartStatus status;
    private List<UserResponse> members;
    private List<GroupCartItemResponse> items;
    private LocalDateTime createdAt;
}
