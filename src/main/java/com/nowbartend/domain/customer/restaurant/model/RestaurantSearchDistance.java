package com.nowbartend.domain.customer.restaurant.model;

import com.nowbartend.global.exception.InvalidException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum RestaurantSearchDistance {
    D_100(100),
    D_200(200),
    D_500(500),
    D_1000(1000),
    D_2000(2000),
    D_3000(3000);

    private final int distance;

    public static void contains(int distance) {
        Arrays.stream(values())
                .filter(d -> d.distance == distance)
                .findFirst()
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID_DISTANCE_REQUEST));
    }
}
