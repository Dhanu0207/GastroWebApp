package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.enums.AppRole;
import com.fooddelivery.foodbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole roleName);

}