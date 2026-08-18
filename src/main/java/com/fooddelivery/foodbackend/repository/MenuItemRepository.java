package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // ─── Optimized single-query lookups with JOIN FETCH (eliminates N+1) ────────

    /**
     * Fetch all items for a restaurant in ONE query (JOIN FETCH avoids N+1 on
     * category and restaurant lazy proxies). Queries directly by FK ID — no
     * parent entity lookup required.
     */
    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE r.restaurantId = :restaurantId
            """)
    List<MenuItem> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    /**
     * Fetch all items for a category in ONE query with JOIN FETCH.
     */
    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE c.categoryId = :categoryId
            """)
    List<MenuItem> findByCategoryId(@Param("categoryId") Long categoryId);

    // ─── Keyword search ──────────────────────────────────────────────────────────

    /**
     * Full-text style LIKE search across itemName and description, scoped to a
     * restaurant. Returns a Page for pagination support.
     */
    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE r.restaurantId = :restaurantId
              AND (LOWER(m.itemName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<MenuItem> searchByKeyword(
            @Param("restaurantId") Long restaurantId,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * Global keyword search (across all restaurants), paginated.
     */
    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE LOWER(m.itemName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<MenuItem> searchGlobal(
            @Param("keyword") String keyword,
            Pageable pageable);

    // ─── Filter queries ──────────────────────────────────────────────────────────

    /**
     * Filtered menu fetch with optional available, isVeg, maxPrice filters,
     * scoped to a restaurant. Paginated.
     *
     * Pass null for any parameter to skip that filter.
     */
    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE r.restaurantId = :restaurantId
              AND (:available IS NULL OR m.available = :available)
              AND (:isVeg IS NULL OR m.isVeg = :isVeg)
              AND (:maxPrice IS NULL OR m.price <= :maxPrice)
            """)
    Page<MenuItem> findByRestaurantIdWithFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("available") Boolean available,
            @Param("isVeg") Boolean isVeg,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // ─── Single item with JOIN FETCH (avoids lazy load on detail view) ───────────

    @Query("""
            SELECT m FROM MenuItem m
            JOIN FETCH m.restaurant r
            JOIN FETCH m.category c
            WHERE m.menuItemId = :menuItemId
            """)
    Optional<MenuItem> findByIdWithDetails(@Param("menuItemId") Long menuItemId);
}