package com.nowbartend.domain.owner.restaurant.dto.request;

import jakarta.validation.constraints.Min;

public record RestaurantProfileUpdateRequest(
        String name,
        String introduce,

        @Min(0)
        Integer deposit
) {
}
