package com.nowbartend.domain.owner.restaurant.repository;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRestaurantRepository extends JpaRepository<Restaurant, Long> {
}
