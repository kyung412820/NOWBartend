package com.nowbartend.domain.owner.restaurant.waiting.repository;

import com.nowbartend.domain.owner.restaurant.waiting.dto.response.WaitingDetails;

import java.util.List;

public interface OwnerRestaurantWaitingQRepository {

    List<WaitingDetails> findWaitingDetails(List<Long> userIds);
}
