package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.CartItemRequest;
import com.fooddelivery.foodbackend.dto.request.GroupCartCreateRequest;
import com.fooddelivery.foodbackend.dto.request.OrderRequest;
import com.fooddelivery.foodbackend.dto.response.*;
import com.fooddelivery.foodbackend.entity.*;
import com.fooddelivery.foodbackend.entity.enums.GroupCartStatus;
import com.fooddelivery.foodbackend.entity.enums.OrderStatus;
import com.fooddelivery.foodbackend.entity.enums.PaymentStatus;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.*;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.OrderItemService;
import com.fooddelivery.foodbackend.service.services.GroupCartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupCartServiceImpl implements GroupCartService {

    private static final int MAX_GROUPS_PER_USER = 5;
    private static final int MAX_USERS_PER_GROUP = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GroupCartRepository groupCartRepository;
    private final GroupCartItemRepository groupCartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemService orderItemService;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;

    @Value("${app.order.delivery-fee:40.00}")
    private BigDecimal deliveryFee;

    @Value("${app.order.tax-amount:25.00}")
    private BigDecimal taxAmount;

    @Override
    public GroupCartResponse createGroupCart(GroupCartCreateRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        validateUserGroupLimit(currentUser);

        String inviteCode = generateUniqueInviteCode();

        GroupCart groupCart = GroupCart.builder()
                .groupName(request.getGroupName())
                .inviteCode(inviteCode)
                .hostUser(currentUser)
                .status(GroupCartStatus.ACTIVE)
                .totalPrice(BigDecimal.ZERO)
                .totalItems(0)
                .build();

        groupCart.getMembers().add(currentUser);

        GroupCart savedCart = groupCartRepository.save(groupCart);

        return mapToResponse(savedCart);
    }

    @Override
    public GroupCartResponse joinGroupCart(String inviteCode) {
        User currentUser = securityUtils.getCurrentUser();

        GroupCart groupCart = groupCartRepository.findByInviteCode(inviteCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("GroupCart", "inviteCode", inviteCode));

        if (groupCart.getStatus() != GroupCartStatus.ACTIVE) {
            throw new BadRequestException("This group cart is no longer active.");
        }

        if (groupCart.getMembers().contains(currentUser)) {
            throw new BadRequestException("You are already a member of this group cart.");
        }

        if (groupCart.getMembers().size() >= MAX_USERS_PER_GROUP) {
            throw new BadRequestException("Group cart has reached the maximum capacity of " + MAX_USERS_PER_GROUP + " users.");
        }

        validateUserGroupLimit(currentUser);

        groupCart.getMembers().add(currentUser);

        GroupCart updatedCart = groupCartRepository.save(groupCart);

        return mapToResponse(updatedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupCartResponse getGroupCart(Long groupCartId) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);
        return mapToResponse(groupCart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupCartResponse> getMyGroupCarts() {
        User currentUser = securityUtils.getCurrentUser();
        List<GroupCart> groupCarts = groupCartRepository.findByMembersContainingAndStatus(currentUser, GroupCartStatus.ACTIVE);
        return groupCarts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GroupCartResponse addItemToGroupCart(Long groupCartId, CartItemRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);

        if (groupCart.getStatus() != GroupCartStatus.ACTIVE) {
            throw new BadRequestException("Cannot add items to an inactive group cart.");
        }

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", request.getMenuItemId()));

        if (menuItem.getAvailable() != null && !menuItem.getAvailable()) {
            throw new BadRequestException("Menu item is currently unavailable.");
        }

        // Single restaurant validation
        if (groupCart.getRestaurant() == null) {
            groupCart.setRestaurant(menuItem.getRestaurant());
        } else if (!groupCart.getRestaurant().getRestaurantId().equals(menuItem.getRestaurant().getRestaurantId())) {
            throw new BadRequestException("All items in a group cart must belong to the same restaurant: "
                    + groupCart.getRestaurant().getRestaurantName());
        }

        Optional<GroupCartItem> existingItemOpt = groupCartItemRepository
                .findByGroupCartAndMenuItemAndAddedBy(groupCart, menuItem, currentUser);

        if (existingItemOpt.isPresent()) {
            GroupCartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setSubtotal(existingItem.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            groupCartItemRepository.save(existingItem);
        } else {
            GroupCartItem newItem = GroupCartItem.builder()
                    .groupCart(groupCart)
                    .menuItem(menuItem)
                    .addedBy(currentUser)
                    .quantity(request.getQuantity())
                    .price(menuItem.getPrice())
                    .subtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .build();
            groupCart.getItems().add(newItem);
        }

        recalculateCartTotals(groupCart);
        GroupCart updatedCart = groupCartRepository.save(groupCart);

        return mapToResponse(updatedCart);
    }

    @Override
    public GroupCartResponse updateGroupCartItem(Long groupCartId, Long itemId, CartItemRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);

        if (groupCart.getStatus() != GroupCartStatus.ACTIVE) {
            throw new BadRequestException("Cannot update items in an inactive group cart.");
        }

        GroupCartItem cartItem = groupCart.getItems().stream()
                .filter(item -> item.getGroupCartItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("GroupCartItem", "itemId", itemId));

        if (request.getQuantity() <= 0) {
            groupCart.getItems().remove(cartItem);
        } else {
            cartItem.setQuantity(request.getQuantity());
            cartItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        }

        recalculateCartTotals(groupCart);
        GroupCart updatedCart = groupCartRepository.save(groupCart);

        return mapToResponse(updatedCart);
    }

    @Override
    public GroupCartResponse removeItemFromGroupCart(Long groupCartId, Long itemId) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);

        if (groupCart.getStatus() != GroupCartStatus.ACTIVE) {
            throw new BadRequestException("Cannot remove items from an inactive group cart.");
        }

        GroupCartItem cartItem = groupCart.getItems().stream()
                .filter(item -> item.getGroupCartItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("GroupCartItem", "itemId", itemId));

        groupCart.getItems().remove(cartItem);

        recalculateCartTotals(groupCart);
        GroupCart updatedCart = groupCartRepository.save(groupCart);

        return mapToResponse(updatedCart);
    }

    @Override
    public void leaveGroupCart(Long groupCartId) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);

        if (groupCart.getHostUser().getUserId().equals(currentUser.getUserId())) {
            // If host leaves, cancel or close the group cart
            groupCart.setStatus(GroupCartStatus.CANCELLED);
        } else {
            groupCart.getMembers().remove(currentUser);
        }

        groupCartRepository.save(groupCart);
    }

    @Override
    public OrderResponse checkoutGroupCart(Long groupCartId, OrderRequest orderRequest) {
        User currentUser = securityUtils.getCurrentUser();
        GroupCart groupCart = findGroupCartAndValidateMember(groupCartId, currentUser);

        // Host payment constraint check
        if (!groupCart.getHostUser().getUserId().equals(currentUser.getUserId())) {
            throw new BadRequestException("Only the group host can checkout and pay the bill for this group cart.");
        }

        if (groupCart.getStatus() != GroupCartStatus.ACTIVE) {
            throw new BadRequestException("Group cart is not active for checkout.");
        }

        if (groupCart.getItems().isEmpty()) {
            throw new BadRequestException("Group cart is empty.");
        }

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", orderRequest.getAddressId()));

        Order order = new Order();
        order.setUser(currentUser); // Host pays
        order.setRestaurant(groupCart.getRestaurant());
        order.setDeliveryAddress(address);
        order.setOrderNumber("GRP" + System.currentTimeMillis());
        order.setSubtotal(groupCart.getTotalPrice());
        order.setDeliveryFee(deliveryFee);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(groupCart.getTotalPrice()
                .add(deliveryFee)
                .add(taxAmount));

        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderNotes(orderRequest.getOrderNotes());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = groupCart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setMenuItem(cartItem.getMenuItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setSubtotal(cartItem.getSubtotal());
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        groupCart.setStatus(GroupCartStatus.CHECKED_OUT);
        groupCartRepository.save(groupCart);

        OrderResponse response = modelMapper.map(savedOrder, OrderResponse.class);
        response.setOrderItems(orderItemService.getOrderItems(savedOrder.getOrderId()));

        return response;
    }

    private void validateUserGroupLimit(User user) {
        int activeGroupCount = groupCartRepository.countByMembersContainingAndStatus(user, GroupCartStatus.ACTIVE);
        if (activeGroupCount >= MAX_GROUPS_PER_USER) {
            throw new BadRequestException("User cannot belong to more than " + MAX_GROUPS_PER_USER + " active group carts.");
        }
    }

    private GroupCart findGroupCartAndValidateMember(Long groupCartId, User user) {
        GroupCart groupCart = groupCartRepository.findById(groupCartId)
                .orElseThrow(() -> new ResourceNotFoundException("GroupCart", "groupCartId", groupCartId));

        if (!groupCart.getMembers().contains(user)) {
            throw new BadRequestException("You are not a member of this group cart.");
        }

        return groupCart;
    }

    private void recalculateCartTotals(GroupCart groupCart) {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        for (GroupCartItem item : groupCart.getItems()) {
            total = total.add(item.getSubtotal());
            count += item.getQuantity();
        }

        groupCart.setTotalPrice(total);
        groupCart.setTotalItems(count);

        if (count == 0) {
            groupCart.setRestaurant(null);
        }
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
            }
            code = sb.toString();
        } while (groupCartRepository.existsByInviteCode(code));
        return code;
    }

    private GroupCartResponse mapToResponse(GroupCart groupCart) {
        UserResponse hostResponse = modelMapper.map(groupCart.getHostUser(), UserResponse.class);

        List<UserResponse> memberResponses = groupCart.getMembers().stream()
                .map(member -> modelMapper.map(member, UserResponse.class))
                .collect(Collectors.toList());

        List<GroupCartItemResponse> itemResponses = new ArrayList<>();
        if (groupCart.getItems() != null) {
            for (GroupCartItem item : groupCart.getItems()) {
                GroupCartItemResponse itemResp = GroupCartItemResponse.builder()
                        .groupCartItemId(item.getGroupCartItemId())
                        .menuItemId(item.getMenuItem().getMenuItemId())
                        .menuItemName(item.getMenuItem().getItemName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .addedByUserId(item.getAddedBy().getUserId())
                        .addedByUserName(item.getAddedBy().getFirstName() + " " + item.getAddedBy().getLastName())
                        .build();
                itemResponses.add(itemResp);
            }
        }

        return GroupCartResponse.builder()
                .groupCartId(groupCart.getGroupCartId())
                .groupName(groupCart.getGroupName())
                .inviteCode(groupCart.getInviteCode())
                .hostUser(hostResponse)
                .restaurantId(groupCart.getRestaurant() != null ? groupCart.getRestaurant().getRestaurantId() : null)
                .restaurantName(groupCart.getRestaurant() != null ? groupCart.getRestaurant().getRestaurantName() : null)
                .totalPrice(groupCart.getTotalPrice())
                .totalItems(groupCart.getTotalItems())
                .status(groupCart.getStatus())
                .members(memberResponses)
                .items(itemResponses)
                .createdAt(groupCart.getCreatedAt())
                .build();
    }
}
