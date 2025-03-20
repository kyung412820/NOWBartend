package com.nowbartend.domain.customer.restaurant.controller;

import com.nowbartend.domain.customer.restaurant.dto.response.RestaurantResponse;
import com.nowbartend.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.nowbartend.domain.customer.restaurant.service.CustomerRestaurantService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer/api/restaurants")
@RequiredArgsConstructor
public class CustomerRestaurantController {

    private final CustomerRestaurantService customerRestaurantService;

    @GetMapping
    public ResponseEntity<Slice<RestaurantSimpleResponse>> findRestaurants(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 30) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long seatTypeId,
            @RequestParam(required = false) Boolean isUseDistance,
            @RequestParam(required = false) Double clientLat,
            @RequestParam(required = false) Double clientLon,
            @RequestParam(required = false) Integer distance,
            @RequestParam(required = false) String order
    ) {
        Slice<RestaurantSimpleResponse> response = customerRestaurantService.findRestaurants(
                userDetails,
                pageable,
                keyword,
                address,
                minPrice,
                maxPrice,
                seatTypeId,
                isUseDistance,
                clientLat,
                clientLon,
                distance,
                order
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurant(
            @PathVariable Long restaurantId
    ) {
        RestaurantResponse restaurant = customerRestaurantService.findRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
    }
}
