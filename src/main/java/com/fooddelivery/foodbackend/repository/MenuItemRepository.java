package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Category;
import com.fooddelivery.foodbackend.entity.MenuItem;
import com.fooddelivery.foodbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurant(Restaurant restaurant);

    List<MenuItem> findByCategory(Category category);

}