package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.request.GroupCartCreateRequest;
import com.fooddelivery.foodbackend.dto.request.OrderRequest;
import com.fooddelivery.foodbackend.dto.response.GroupCartResponse;
import com.fooddelivery.foodbackend.dto.response.OrderResponse;

import java.util.List;

public interface GroupCartService {

    GroupCartResponse createGroupCart(GroupCartCreateRequest request);

    GroupCartResponse joinGroupCart(String inviteCode);

    GroupCartResponse getGroupCart(Long groupCartId);

    List<GroupCartResponse> getMyGroupCarts();

    GroupCartResponse addItemToGroupCart(Long groupCartId, CartItemRequest request);

    GroupCartResponse updateGroupCartItem(Long groupCartId, Long itemId, CartItemRequest request);

    GroupCartResponse removeItemFromGroupCart(Long groupCartId, Long itemId);

    void leaveGroupCart(Long groupCartId);

    OrderResponse checkoutGroupCart(Long groupCartId, OrderRequest orderRequest);
}
