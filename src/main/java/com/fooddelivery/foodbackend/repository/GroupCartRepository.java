package com.fooddelivery.foodbackend.repository;

import com.fooddelivery.foodbackend.entity.GroupCart;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.entity.enums.GroupCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupCartRepository extends JpaRepository<GroupCart, Long> {

    Optional<GroupCart> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    int countByMembersContainingAndStatus(User user, GroupCartStatus status);

    List<GroupCart> findByMembersContainingAndStatus(User user, GroupCartStatus status);
}
