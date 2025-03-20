package com.nowbartend.domain.customer.restaurant.waiting.repository;

import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingType;

import java.util.Optional;

public interface CustomerRestaurantWaitingQueryDslRepository {

    Optional<Integer> findTodayLastSequence(Long restaurantId, WaitingType waitingType);
}
