package com.nowbartend.domain.customer.coupon.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.coupon.dto.UserCouponResponse;
import com.nowbartend.domain.customer.coupon.entity.UserCoupon;
import com.nowbartend.domain.customer.coupon.repository.UserCouponRepository;
import com.nowbartend.domain.owner.coupon.dto.request.CouponResponse;
import com.nowbartend.domain.owner.coupon.entity.Coupon;
import com.nowbartend.domain.owner.coupon.entity.CouponType;
import com.nowbartend.domain.owner.coupon.repository.CouponRepository;
import com.nowbartend.global.aop.annotation.DistributedLock;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public List<CouponResponse> findRestaurantCoupons(Long restaurantId) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(restaurantId);

        return coupons.stream()
                .filter(coupon -> {
                    if (coupon.getType() == CouponType.FIRST_COME) {
                        Integer maxCount = coupon.getFirstComeCouponMaxCount();

                        if (maxCount == null) {
                            maxCount = 0;
                        }

                        // 선착순 이벤트가 끝난 쿠폰은 유저가 조회하면 안된다.
                        long downloadedCount = userCouponRepository.countByCouponIdAndCouponUsedFalse(coupon.getId());
                        return downloadedCount < maxCount;
                    }
                    return true;
                })
                .map(CouponResponse::from)
                .toList();
    }

    @DistributedLock(key = "'coupon:' + #couponId")
    @Transactional
    public void downloadCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));


        if (userCouponRepository.findByUserIdAndCouponId(userId, couponId).isPresent()) {
            throw new CustomRuntimeException(ErrorCode.COUPON_ALREADY_DOWNLOAD);
        }


        UserCoupon userCoupon = UserCoupon.builder()
                .couponUsed(false)
                .user(user)
                .coupon(coupon)
                .build();

        if (coupon.getType() == CouponType.FIRST_COME) {
            long downloadedCount = userCouponRepository.countByCouponId(couponId);

            // 다운로드 한도 초과 여부 체크
            if (downloadedCount >= coupon.getFirstComeCouponMaxCount()) {
                userCoupon.setCouponUsed(true);
                throw new CustomRuntimeException(ErrorCode.COUPON_LIMIT_EXCEEDED);
            }
        }

        userCouponRepository.save(userCoupon);

    }

    public List<UserCouponResponse> findUserCoupons(Long userId, Long cursor, int size) {

        List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponUsedFalseAndIdGreaterThan(userId, cursor, size);

        return userCoupons.stream()
                .map(UserCouponResponse::from)
                .toList();
    }

    @Transactional
    public void useCoupon(Long userId, Long couponId) {
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

        if (userCoupon.getCouponUsed()) {
            throw new CustomRuntimeException(ErrorCode.COUPON_ALREADY_USED);
        }

        userCoupon.setCouponUsed(true);

    }
}
