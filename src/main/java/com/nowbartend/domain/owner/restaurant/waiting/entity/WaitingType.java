package com.nowbartend.domain.owner.restaurant.waiting.entity;

import com.nowbartend.global.exception.BadRequest;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WaitingType {
    IN("waiting:in:");

    private final String prefix;

    public static WaitingType of(String waitingType) {
        return Arrays.stream(WaitingType.values())
                .filter(w -> w.name().equalsIgnoreCase(waitingType))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_WAITING_TYPE));
    }

    public String getWaitingKey(Long restaurantId) {
        return this.prefix + "queue:" + restaurantId;
    }
}

