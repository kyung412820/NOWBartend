package com.nowbartend.domain.owner.restaurant.controller;

import com.nowbartend.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.nowbartend.domain.owner.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.nowbartend.domain.owner.restaurant.dto.response.RestaurantFindResponse;
import com.nowbartend.domain.owner.restaurant.service.OwnerRestaurantService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/owner/api/restaurants")
@RequiredArgsConstructor
public class OwnerRestaurantController {

    private final OwnerRestaurantService ownerRestaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RestaurantCreateRequest restaurantCreateRequest
    ) {
        Long restaurantId = ownerRestaurantService.createRestaurant(userDetails.getId(), restaurantCreateRequest);
        URI location = UriBuilderUtil.create("/owner/api/restaurants/{restaurantId}", restaurantId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantFindResponse> findRestaurant(
            @PathVariable Long restaurantId
    ) {
        RestaurantFindResponse restaurant = ownerRestaurantService.findRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId,
            @RequestBody @Valid RestaurantProfileUpdateRequest restaurantProfileUpdateRequest
    ) {
        ownerRestaurantService.updateRestaurantProfile(userDetails.getId(), restaurantId, restaurantProfileUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId
    ) {
        ownerRestaurantService.deleteRestaurant(userDetails.getId(), restaurantId);

        return ResponseEntity.noContent().build();
    }
}
