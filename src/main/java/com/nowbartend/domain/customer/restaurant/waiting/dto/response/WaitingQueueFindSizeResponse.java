package com.nowbartend.domain.customer.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueFindSizeResponse(
        Long waitingQueueSize
) {

    public static WaitingQueueFindSizeResponse from(Long waitingQueueSize) {
        return WaitingQueueFindSizeResponse.builder()
                .waitingQueueSize(waitingQueueSize)
                .build();
    }
}
