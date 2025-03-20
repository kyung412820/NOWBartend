package com.nowbartend.domain.customer.reservation.entity;

import com.nowbartend.global.exception.BadRequest;
import com.nowbartend.global.exception.error.ErrorCode;

import java.util.Arrays;

public enum ReservationStatus {

    COMPLETED, RESERVED, CANCELED;

    public static ReservationStatus of(String status) {
        return Arrays.stream(ReservationStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_RESERVATION_STATUS));
    }
}
