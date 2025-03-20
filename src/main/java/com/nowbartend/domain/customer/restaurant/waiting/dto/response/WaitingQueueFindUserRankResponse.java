package com.nowbartend.domain.customer.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueFindUserRankResponse(
        Long rank
) {

    public static WaitingQueueFindUserRankResponse from(Long rank) {
        return WaitingQueueFindUserRankResponse.builder()
                .rank(rank)
                .build();
    }
}
