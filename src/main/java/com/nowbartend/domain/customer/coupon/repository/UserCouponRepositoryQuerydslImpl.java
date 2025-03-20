package com.nowbartend.domain.customer.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.coupon.entity.UserCoupon;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.nowbartend.domain.customer.coupon.entity.QUserCoupon.userCoupon;


@RequiredArgsConstructor
public class UserCouponRepositoryQuerydslImpl implements UserCouponRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserCoupon> findByUserIdAndCouponUsedFalseAndIdGreaterThan(Long userId, Long cursor, int size) {

        return queryFactory
                .selectFrom(userCoupon)
                .where(
                        userCoupon.user.id.eq(userId),
                        userCoupon.couponUsed.isFalse(),
                        cursor != null ? userCoupon.id.gt(cursor) : null
                )
                .orderBy(userCoupon.id.asc())
                .limit(size)
                .fetch();
    }
}
