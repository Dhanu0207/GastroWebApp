package com.fooddelivery.foodbackend.config;

import com.fooddelivery.foodbackend.entity.Role;
import com.fooddelivery.foodbackend.entity.enums.AppRole;
import com.fooddelivery.foodbackend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds all required roles into the DB on startup if they don't already exist.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // All four roles must exist so registrations and assignments never fail
        createRoleIfNotExists(AppRole.ROLE_USER);
        createRoleIfNotExists(AppRole.ROLE_ADMIN);
        createRoleIfNotExists(AppRole.ROLE_RESTAURANT_OWNER);   // was missing
        createRoleIfNotExists(AppRole.ROLE_DELIVERY_PARTNER);   // was missing
    }

    private void createRoleIfNotExists(AppRole roleName) {
        if (roleRepository.findByRoleName(roleName).isEmpty()) {
            Role role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
    }
}
