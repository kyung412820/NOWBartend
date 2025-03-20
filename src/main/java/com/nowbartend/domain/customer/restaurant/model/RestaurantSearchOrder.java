package com.nowbartend.domain.customer.restaurant.model;

import java.util.Arrays;

public enum RestaurantSearchOrder {

    POPULARITY,
    NEARBY;

    public static RestaurantSearchOrder from(String value) {
        return value != null && contains(value)
                ? RestaurantSearchOrder.valueOf(value.toUpperCase())
                : POPULARITY;
    }

    public static boolean contains(String value) {
        return Arrays.stream(values())
                .anyMatch(order -> order.name().equalsIgnoreCase(value));
    }
}
