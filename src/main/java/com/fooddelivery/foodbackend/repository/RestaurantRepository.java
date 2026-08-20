package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByEmail(String email);

    boolean existsByEmail(String email);

    /** Public listing — only return approved & open restaurants, paginated. */
    Page<Restaurant> findByIsApprovedTrueAndIsOpenTrue(Pageable pageable);

    /** Admin listing — all restaurants (approved or not), paginated. */
    Page<Restaurant> findAll(Pageable pageable);


    @Query("""
            SELECT r FROM Restaurant r
            WHERE r.isApproved = true
              AND (LOWER(r.restaurantName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(r.city) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Restaurant> searchApprovedRestaurants(
            @Param("keyword") String keyword,
            Pageable pageable);


    List<Restaurant> findByIsApprovedFalse();
}