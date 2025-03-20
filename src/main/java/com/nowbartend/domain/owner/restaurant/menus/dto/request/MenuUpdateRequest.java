package com.nowbartend.domain.owner.restaurant.menus.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record MenuUpdateRequest(
        String menuName,

        @Min(0)
        Integer menuPrice,

        @Size(max = 100)
        String menuDescription
) {
}
