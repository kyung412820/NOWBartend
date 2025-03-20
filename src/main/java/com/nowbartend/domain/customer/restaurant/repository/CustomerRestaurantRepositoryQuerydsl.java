package com.nowbartend.domain.customer.restaurant.repository;

import com.nowbartend.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.nowbartend.global.security.entity.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomerRestaurantRepositoryQuerydsl {

    Slice<RestaurantSimpleResponse> findRestaurantsByFilters(
            CustomUserDetails userDetails, Pageable pageable,
            String keyword, String address,
            Integer minPrice, Integer maxPrice,
            Long seatTypeId, Boolean isUseDistance,
            Double clientLat, Double clientLon,
            Integer distance, String order
    );
}
