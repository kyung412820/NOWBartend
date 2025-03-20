package com.nowbartend.domain.customer.restaurant.waiting.dto.request;

import jakarta.validation.constraints.NotNull;

public record WaitingJoinRequest(
        @NotNull
        Integer totalCount
) {
}
