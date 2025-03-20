package com.nowbartend.domain.owner.coupon.controller;

import com.nowbartend.domain.owner.coupon.dto.request.CouponCreateRequest;
import com.nowbartend.domain.owner.coupon.dto.request.CouponResponse;
import com.nowbartend.domain.owner.coupon.service.OwnerCouponService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owner/api/restaurants/{restaurantId}/coupons")
@RequiredArgsConstructor
public class OwnerCouponController {

    private final OwnerCouponService ownerCouponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @PathVariable Long restaurantId,
            @RequestBody CouponCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CouponResponse response = ownerCouponService.createCoupon(userDetails.getId(), restaurantId, request);

        URI location = UriBuilderUtil.create("/owner/api/restaurants/{restaurantId}", restaurantId);

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ownerCouponService.findCoupons(restaurantId));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable Long restaurantId,
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {
        ownerCouponService.deleteCoupon(restaurantId, couponId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
