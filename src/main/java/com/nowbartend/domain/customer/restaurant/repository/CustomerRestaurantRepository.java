package com.nowbartend.domain.customer.restaurant.repository;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRestaurantRepository extends JpaRepository<Restaurant, Long>, CustomerRestaurantRepositoryQuerydsl {
}
