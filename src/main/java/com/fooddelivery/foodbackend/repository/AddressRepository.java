package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Address;
import com.fooddelivery.foodbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    List<Address> findByUserUserId(Long userId);

    Address findByUserAndIsDefaultTrue(User user);

}