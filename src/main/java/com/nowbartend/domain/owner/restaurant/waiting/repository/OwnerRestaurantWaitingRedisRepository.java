package com.nowbartend.domain.owner.restaurant.waiting.repository;

import java.util.List;
import java.util.Optional;

public interface OwnerRestaurantWaitingRedisRepository {

    List<Long> findWaitingUsers(String key, int page, int size);

    Optional<Long> findFirstRankedUser(String key);

    Long findQueueSize(String key);

    Long deleteWaitingUser(String key, Long userId);
}
