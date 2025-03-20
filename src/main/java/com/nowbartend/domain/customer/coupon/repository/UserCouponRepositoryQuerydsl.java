package com.nowbartend.domain.customer.coupon.repository;

import com.nowbartend.domain.customer.coupon.entity.UserCoupon;

import java.util.List;

public interface UserCouponRepositoryQuerydsl {
    List<UserCoupon> findByUserIdAndCouponUsedFalseAndIdGreaterThan(Long userId, Long cursor, int size);
}
