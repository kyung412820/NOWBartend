package com.nowbartend.domain.owner.coupon.dto.request;

import com.nowbartend.domain.owner.coupon.entity.Coupon;
import jakarta.validation.constraints.NotNull;

public record CouponResponse(
        @NotNull
        Long couponId,

        @NotNull
        String couponName,

        @NotNull
        Integer discount
) {

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscount()
        );
    }
}
