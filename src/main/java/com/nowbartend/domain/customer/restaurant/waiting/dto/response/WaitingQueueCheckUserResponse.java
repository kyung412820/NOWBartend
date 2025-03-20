package com.nowbartend.domain.customer.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueCheckUserResponse(
        boolean status
) {

    public static WaitingQueueCheckUserResponse from(boolean status) {
        return WaitingQueueCheckUserResponse.builder()
                .status(status)
                .build();
    }
}
