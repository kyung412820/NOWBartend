package com.nowbartend.domain.owner.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class OwnerRestaurantWaitingRedisRepositoryImpl implements OwnerRestaurantWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<Long> findWaitingUsers(String key, int page, int size) {
        long start = (long) page * size;
        long end = start + size - 1;

        Set<String> queue = redisTemplate.opsForZSet().range(key, start, end);

        if (queue == null || queue.isEmpty()) {
            return List.of();
        }

        return queue.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public Optional<Long> findFirstRankedUser(String key) {
        Set<String> queue = redisTemplate.opsForZSet().range(key, 0, 0);

        if (queue == null || queue.isEmpty()) {
            return Optional.empty();
        }

        return queue.stream()
                .map(Long::parseLong)
                .findFirst();
    }

    @Override
    public Long findQueueSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Long deleteWaitingUser(String key, Long userId) {
        return redisTemplate.opsForZSet().remove(key, userId.toString());
    }
}
