package com.nowbartend.domain.owner.coupon.dto.request;

import com.nowbartend.domain.owner.coupon.entity.CouponType;
import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest(
        @NotNull
        String couponName,

        @NotNull
        Integer discount,

        @NotNull
        Integer firstComeCouponMaxCount,

        @NotNull
        CouponType type

) {
}
