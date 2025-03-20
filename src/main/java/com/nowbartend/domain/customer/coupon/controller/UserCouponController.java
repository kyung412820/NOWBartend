package com.nowbartend.domain.customer.coupon.controller;

import com.nowbartend.domain.customer.coupon.dto.UserCouponResponse;
import com.nowbartend.domain.customer.coupon.service.UserCouponService;
import com.nowbartend.domain.owner.coupon.dto.request.CouponResponse;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/api/coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService usercouponService;

    @GetMapping()
    public ResponseEntity<List<CouponResponse>> findRestaurantCoupons(
            @RequestParam Long restaurantId
    ) {
        List<CouponResponse> coupons = usercouponService.findRestaurantCoupons(restaurantId);
        return ResponseEntity.ok(coupons);
    }

    @PostMapping("/{couponId}/download")
    public ResponseEntity<Void> downloadCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        usercouponService.downloadCoupon(userDetails.getId(), couponId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/download")
    public ResponseEntity<List<UserCouponResponse>> findUserCoupons(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size

    ) {
        return ResponseEntity.ok(usercouponService.findUserCoupons(userDetails.getId(), cursor, size));
    }

    @DeleteMapping("/{couponId}/use")
    public ResponseEntity<Void> useCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        usercouponService.useCoupon(userDetails.getId(), couponId);

        return ResponseEntity.noContent().build();
    }
}