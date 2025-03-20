package com.nowbartend.domain.customer.coupon.dto;

import com.nowbartend.domain.customer.coupon.entity.UserCoupon;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UserCouponResponse(
        @NotNull
        Long couponId,

        @NotNull
        String couponName,

        @NotNull
        Integer discount,

        @NotNull
        Boolean couponUsed
) implements Serializable {

    public static UserCouponResponse from(UserCoupon usercoupon) {
        return new UserCouponResponse(
                usercoupon.getId(),
                usercoupon.getCoupon().getCouponName(),
                usercoupon.getCoupon().getDiscount(),
                usercoupon.getCouponUsed()
        );
    }
}