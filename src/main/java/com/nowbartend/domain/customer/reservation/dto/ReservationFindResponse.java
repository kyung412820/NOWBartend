package com.nowbartend.domain.customer.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nowbartend.domain.customer.reservation.entity.Reservation;
import com.nowbartend.domain.customer.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record ReservationFindResponse(
        @NotNull
        Long reservationId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        LocalDateTime date,

        @NotNull
        Integer totalCount,

        @NotNull
        ReservationStatus status,

        @NotNull
        Long restaurantId,

        @NotBlank
        String restaurantName,

        @NotBlank
        String restaurantAddress,

        @NotNull
        Long seatTypeId,

        @NotBlank
        String seatTypeName
) {

    public static ReservationFindResponse from(Reservation reservation) {
        return ReservationFindResponse.builder()
                .reservationId(reservation.getId())
                .date(reservation.getDate())
                .totalCount(reservation.getTotalCount())
                .status(reservation.getStatus())
                .restaurantId(reservation.getRestaurantSeat().getRestaurant().getId())
                .restaurantName(reservation.getRestaurantSeat().getRestaurant().getName())
                .restaurantAddress(reservation.getRestaurantSeat().getRestaurant().getAddress())
                .seatTypeId(reservation.getRestaurantSeat().getSeatType().getId())
                .seatTypeName(reservation.getRestaurantSeat().getSeatType().getName())
                .build();
    }
}
