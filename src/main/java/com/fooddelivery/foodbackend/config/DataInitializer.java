package com.fooddelivery.foodbackend.config;

import com.fooddelivery.foodbackend.entity.AppRole;
import com.fooddelivery.foodbackend.entity.Role;
import com.fooddelivery.foodbackend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public void run(String... args) {
        createRoleIfNotExists(AppRole.ROLE_ADMIN);

        createRoleIfNotExists(AppRole.ROLE_USER);
    }
    private void createRoleIfNotExists(AppRole roleName) {
        if(roleRepository.findByRoleName(roleName).isEmpty()) {
            Role role =new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);

        }
    }
}
