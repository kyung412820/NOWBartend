package com.nowbartend.domain.customer.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class CustomerRestaurantWaitingRedisRepositoryImpl implements CustomerRestaurantWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void join(String queueKey, Long userId, Integer score) {
        redisTemplate.opsForZSet().add(queueKey, userId.toString(), score);
    }

    @Override
    public Optional<Integer> zFindLastSequence(String key) {
        Set<ZSetOperations.TypedTuple<String>> records = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 0);

        if (records == null || records.isEmpty()) {
            return Optional.empty();
        }

        Double score = records.iterator().next().getScore();

        if (score == null) {
            return Optional.empty();
        }

        return Optional.of(score.intValue());
    }

    @Override
    public Optional<Long> zFindUserRank(String key, Long userId) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rank(key, userId.toString()));
    }

    @Override
    public void zRemove(String key, Long userId) {
        redisTemplate.opsForZSet().remove(key, userId.toString());
    }

    @Override
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}
