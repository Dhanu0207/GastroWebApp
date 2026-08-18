package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.request.GroupCartCreateRequest;
import com.fooddelivery.foodbackend.dto.request.OrderRequest;
import com.fooddelivery.foodbackend.dto.response.GroupCartResponse;
import com.fooddelivery.foodbackend.dto.response.OrderResponse;
import com.fooddelivery.foodbackend.service.services.GroupCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-carts")
@RequiredArgsConstructor
public class GroupCartController {

    private final GroupCartService groupCartService;

    @PostMapping
    public ResponseEntity<GroupCartResponse> createGroupCart(
            @Valid @RequestBody GroupCartCreateRequest request) {

        GroupCartResponse response = groupCartService.createGroupCart(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<GroupCartResponse> joinGroupCart(
            @PathVariable String inviteCode) {

        GroupCartResponse response = groupCartService.joinGroupCart(inviteCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupCartResponse>> getMyGroupCarts() {

        List<GroupCartResponse> response = groupCartService.getMyGroupCarts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupCartId}")
    public ResponseEntity<GroupCartResponse> getGroupCart(
            @PathVariable Long groupCartId) {

        GroupCartResponse response = groupCartService.getGroupCart(groupCartId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{groupCartId}/items")
    public ResponseEntity<GroupCartResponse> addItemToGroupCart(
            @PathVariable Long groupCartId,
            @Valid @RequestBody CartItemRequest request) {

        GroupCartResponse response = groupCartService.addItemToGroupCart(groupCartId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{groupCartId}/items/{itemId}")
    public ResponseEntity<GroupCartResponse> updateGroupCartItem(
            @PathVariable Long groupCartId,
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemRequest request) {

        GroupCartResponse response = groupCartService.updateGroupCartItem(groupCartId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupCartId}/items/{itemId}")
    public ResponseEntity<GroupCartResponse> removeItemFromGroupCart(
            @PathVariable Long groupCartId,
            @PathVariable Long itemId) {

        GroupCartResponse response = groupCartService.removeItemFromGroupCart(groupCartId, itemId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupCartId}/leave")
    public ResponseEntity<Void> leaveGroupCart(
            @PathVariable Long groupCartId) {

        groupCartService.leaveGroupCart(groupCartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupCartId}/checkout")
    public ResponseEntity<OrderResponse> checkoutGroupCart(
            @PathVariable Long groupCartId,
            @Valid @RequestBody OrderRequest orderRequest) {

        OrderResponse response = groupCartService.checkoutGroupCart(groupCartId, orderRequest);
        return ResponseEntity.ok(response);
    }
}
