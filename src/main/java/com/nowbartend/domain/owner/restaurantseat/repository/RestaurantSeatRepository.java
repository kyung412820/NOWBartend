package com.nowbartend.domain.owner.restaurantseat.repository;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantSeatRepository extends JpaRepository<RestaurantSeat, Long> {

    void deleteByRestaurant(Restaurant restaurant);

    Optional<RestaurantSeat> findByRestaurantIdAndSeatTypeId(Long restaurantId, Long seatTypeId);
}
