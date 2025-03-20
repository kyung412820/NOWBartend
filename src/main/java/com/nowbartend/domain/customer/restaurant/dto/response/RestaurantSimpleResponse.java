package com.nowbartend.domain.customer.restaurant.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;

@Getter
public class RestaurantSimpleResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final boolean isBookmark;
    private final Double distance;
    private final Double latitude;
    private final Double longitude;

    @QueryProjection
    public RestaurantSimpleResponse(
            Long id,
            String name,
            String address,
            LocalTime openTime,
            LocalTime closeTime,
            Integer minPrice,
            Integer maxPrice,
            boolean isBookmark,
            Double distance,
            Point location
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.isBookmark = isBookmark;
        this.distance = distance;
        this.latitude = location.getY();
        this.longitude = location.getX();
    }
}
