package com.nowbartend.domain.customer.restaurant.dto.response;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;

@Builder(access = AccessLevel.PRIVATE)
public record RestaurantResponse(
        Long restaurantId,
        String name,
        String address,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime breakTimeStart,
        LocalTime breakTimeEnd,
        String introduce,
        Integer deposit,
        LocalTime reservationInterval,
        Double latitude,
        Double longitude
) {

    public static RestaurantResponse from(Restaurant restaurant) {
        Point location = restaurant.getLocation();

        return RestaurantResponse.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .openTime(restaurant.getOpenTime())
                .closeTime(restaurant.getCloseTime())
                .breakTimeStart(restaurant.getBreakTimeStart())
                .breakTimeEnd(restaurant.getBreakTimeEnd())
                .introduce(restaurant.getIntroduce())
                .deposit(restaurant.getDeposit())
                .reservationInterval(restaurant.getReservationInterval())
                .latitude(location.getY())
                .longitude(location.getX())
                .build();
    }
}
