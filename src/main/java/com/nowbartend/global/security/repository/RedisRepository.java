package com.nowbartend.global.security.repository;

import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        if (valueOperations.get(key) == null) {
            throw new NotFoundException(ErrorCode.VALUE_NOT_FOUND_FOR_KEY);
        }

        return valueOperations.get(key);
    }

    public void setDataWithExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, value, duration, TimeUnit.MILLISECONDS);
    }
}
