package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Category;
import com.fooddelivery.foodbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByRestaurant(Restaurant restaurant);

}