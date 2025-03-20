package com.nowbartend.global.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisTemplate.getConnectionFactory());
        builder.withCacheConfiguration("userCoupons",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofDays(365))  // 캐시 만료 시간 1년
        );
        return builder.build();
    }
}
