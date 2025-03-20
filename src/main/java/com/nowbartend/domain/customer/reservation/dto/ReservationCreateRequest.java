package com.nowbartend.domain.customer.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @NotNull
        Long restaurantId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        LocalDateTime date,

        @NotNull
        Integer totalCount,

        @NotNull
        Long seatTypeId
) {
}
