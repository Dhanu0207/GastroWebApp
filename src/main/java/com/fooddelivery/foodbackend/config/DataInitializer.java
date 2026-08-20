package com.fooddelivery.foodbackend.config;

import com.fooddelivery.foodbackend.entity.*;
import com.fooddelivery.foodbackend.entity.enums.AppRole;
import com.fooddelivery.foodbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking & seeding default application data...");

        // 1. Seed Roles
        Role roleUser = getOrCreateRole(AppRole.ROLE_USER);
        Role roleAdmin = getOrCreateRole(AppRole.ROLE_ADMIN);
        Role roleOwner = getOrCreateRole(AppRole.ROLE_RESTAURANT_OWNER);
        Role roleValet = getOrCreateRole(AppRole.ROLE_DELIVERY_PARTNER);

        // 2. Seed Default Accounts
        User customer = getOrCreateUser(
                "Alex", "Chef", "customer@gastro.com", "+919876543210", "password123", Set.of(roleUser)
        );

        User admin = getOrCreateUser(
                "Admin", "Super", "admin@gastro.com", "+919876543211", "password123", Set.of(roleAdmin)
        );

        User owner = getOrCreateUser(
                "Marco", "Owner", "owner@gastro.com", "+919876543212", "password123", Set.of(roleOwner)
        );

        // 3. Seed Restaurants & Menu Items if none exist
        if (restaurantRepository.count() == 0) {
            log.info("Seeding initial restaurants and gourmet menus...");

            Restaurant r1 = restaurantRepository.save(Restaurant.builder()
                    .restaurantName("Gastro Smokehouse & Grill")
                    .email("contact@gastrosmokehouse.com")
                    .phoneNumber("+91 98765 11111")
                    .address("42 Gourmet Boulevard, Downtown")
                    .city("Bengaluru")
                    .state("Karnataka")
                    .pincode("560038")
                    .description("Artisanal prime steaks, slow-smoked burgers & craft cocktails.")
                    .imageUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1000&q=80")
                    .rating(4.9)
                    .isOpen(true)
                    .isApproved(true)
                    .owner(owner)
                    .build());

            Restaurant r2 = restaurantRepository.save(Restaurant.builder()
                    .restaurantName("Kyoto Zen Sushi & Ramen")
                    .email("info@kyotozen.com")
                    .phoneNumber("+91 98765 22222")
                    .address("18 Lotus Lane, Indiranagar")
                    .city("Bengaluru")
                    .state("Karnataka")
                    .pincode("560038")
                    .description("Authentic hand-rolled nigiri, rich tonkotsu ramen and sashimi.")
                    .imageUrl("https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=1000&q=80")
                    .rating(4.8)
                    .isOpen(true)
                    .isApproved(true)
                    .owner(owner)
                    .build());

            // Categories for R1
            Category catBurgers = categoryRepository.save(Category.builder()
                    .categoryName("Burgers & Grills")
                    .description("Slow smoked artisanal burgers")
                    .isActive(true)
                    .restaurant(r1)
                    .build());

            // Menu Items for R1
            menuItemRepository.save(MenuItem.builder()
                    .itemName("Truffle Wagyu Melt Burger")
                    .description("Smoked prime beef patty with truffle aioli, melted aged gruyere on a brioche bun.")
                    .price(new BigDecimal("480.00"))
                    .isVeg(false)
                    .available(true)
                    .preparationTime(20)
                    .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80")
                    .restaurant(r1)
                    .category(catBurgers)
                    .build());

            menuItemRepository.save(MenuItem.builder()
                    .itemName("Crispy Truffle Parmesan Fries")
                    .description("Double-cooked Idaho potatoes tossed in white truffle essence and aged parmesan.")
                    .price(new BigDecimal("220.00"))
                    .isVeg(true)
                    .available(true)
                    .preparationTime(10)
                    .imageUrl("https://images.unsplash.com/photo-1576107232684-1279f3908594?auto=format&fit=crop&w=800&q=80")
                    .restaurant(r1)
                    .category(catBurgers)
                    .build());

            // Categories for R2
            Category catAsian = categoryRepository.save(Category.builder()
                    .categoryName("Sushi & Ramen")
                    .description("Japanese signature bowls")
                    .isActive(true)
                    .restaurant(r2)
                    .build());

            menuItemRepository.save(MenuItem.builder()
                    .itemName("Ahi Tuna & Salmon Poke Bowl")
                    .description("Fresh sushi-grade tuna, salmon, avocado, edamame over seasoned sushi rice.")
                    .price(new BigDecimal("540.00"))
                    .isVeg(false)
                    .available(true)
                    .preparationTime(15)
                    .imageUrl("https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80")
                    .restaurant(r2)
                    .category(catAsian)
                    .build());

            log.info("Default seed data initialized successfully!");
        }
    }

    private Role getOrCreateRole(AppRole appRole) {
        return roleRepository.findByRoleName(appRole)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName(appRole);
                    return roleRepository.save(role);
                });
    }

    private User getOrCreateUser(String first, String last, String email, String phone, String rawPassword, Set<Role> roles) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = User.builder()
                            .firstName(first)
                            .lastName(last)
                            .email(email)
                            .phoneNumber(phone)
                            .password(passwordEncoder.encode(rawPassword))
                            .enabled(true)
                            .roles(roles)
                            .build();
                    return userRepository.save(user);
                });
    }
}
