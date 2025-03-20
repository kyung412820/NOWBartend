package com.nowbartend.domain.owner.coupon.entity;

import com.nowbartend.domain.customer.coupon.entity.UserCoupon;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons",
        indexes = {
                @Index(name = "idx_restaurant_id", columnList = "restaurant_id")
        }
)
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = true)
    private Integer firstComeCouponMaxCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    @Builder
    public Coupon(
            String couponName,
            Integer discount,
            Restaurant restaurant,
            Integer firstComeCouponMaxCount,
            CouponType type
    ) {
        this.couponName = couponName;
        this.discount = discount;
        this.restaurant = restaurant;
        this.firstComeCouponMaxCount = firstComeCouponMaxCount;
        this.type = type;
    }

}
