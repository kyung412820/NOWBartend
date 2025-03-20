package com.nowbartend.domain.owner.restaurant.waiting.entity;

import com.nowbartend.global.exception.BadRequest;
import com.nowbartend.global.exception.error.ErrorCode;

import java.util.Arrays;

public enum WaitingStatus {

    REQUESTED,
    NOTIFIED,
    COMPLETED,
    CANCELED;

    public static WaitingStatus of(String status) {
        return Arrays.stream(WaitingStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_WAITING_STATUS));
    }
}
