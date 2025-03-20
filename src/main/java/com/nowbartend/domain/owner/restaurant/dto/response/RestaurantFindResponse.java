package com.nowbartend.domain.owner.restaurant.dto.response;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record RestaurantFindResponse(
        Long restaurantId,
        Long ownerId,
        String name,
        String address,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime breakTimeStart,
        LocalTime breakTimeEnd,
        String introduce,
        int deposit,
        LocalTime reservationInterval
) {

    public static RestaurantFindResponse from(Restaurant restaurant) {
        return RestaurantFindResponse.builder()
                .restaurantId(restaurant.getId())
                .ownerId(restaurant.getOwner().getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .openTime(restaurant.getOpenTime())
                .closeTime(restaurant.getCloseTime())
                .breakTimeStart(restaurant.getBreakTimeStart())
                .breakTimeEnd(restaurant.getBreakTimeEnd())
                .introduce(restaurant.getIntroduce())
                .deposit(restaurant.getDeposit())
                .reservationInterval(restaurant.getReservationInterval())
                .build();
    }
}
