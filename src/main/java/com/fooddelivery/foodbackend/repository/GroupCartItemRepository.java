package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.GroupCart;
import com.fooddelivery.foodbackend.entity.GroupCartItem;
import com.fooddelivery.foodbackend.entity.MenuItem;
import com.fooddelivery.foodbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupCartItemRepository extends JpaRepository<GroupCartItem, Long> {

    Optional<GroupCartItem> findByGroupCartAndMenuItemAndAddedBy(
            GroupCart groupCart,
            MenuItem menuItem,
            User addedBy
    );
}
