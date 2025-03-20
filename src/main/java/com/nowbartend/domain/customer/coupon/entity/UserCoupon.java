package com.nowbartend.domain.customer.coupon.entity;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.owner.coupon.entity.Coupon;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
        name = "user_coupons",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_coupon_id", columnList = "coupon_id")
        }
)
@Getter
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(nullable = false)
    private Boolean couponUsed = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Builder
    public UserCoupon(Boolean couponUsed, User user, Coupon coupon) {
        this.couponUsed = couponUsed;
        this.user = user;
        this.coupon = coupon;
    }

    public void setCouponUsed(Boolean couponUsed) {
        this.couponUsed = couponUsed;
    }
}
