package com.nowbartend.domain.owner.restaurant.entity;

import com.nowbartend.domain.common.BaseEntity;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.owner.restaurant.menus.entity.Menu;
import com.nowbartend.domain.owner.restaurant.menus.vo.Menus;
import com.nowbartend.global.util.CalculatorUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_fk_owner_id", columnList = "owner_id"),
                @Index(name = "idx_restaurant_min_price", columnList = "minPrice"),
                @Index(name = "idx_restaurant_max_price", columnList = "maxPrice")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private LocalTime breakTimeStart;

    @Column(nullable = false)
    private LocalTime breakTimeEnd;

    @Column(nullable = false)
    private String introduce;

    @Column(nullable = false)
    private int deposit;

    @Column(nullable = false)
    private LocalTime reservationInterval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location;

    @Column(nullable = false)
    @ColumnDefault("'CLOSE'")
    @Enumerated(EnumType.STRING)
    private RestaurantWaitingStatus waitingStatus;

    private Integer minPrice;
    private Integer maxPrice;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Restaurant(
            String name,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime breakTimeStart,
            LocalTime breakTimeEnd,
            String introduce,
            int deposit,
            LocalTime reservationInterval,
            User owner,
            String address,
            Point location
    ) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakTimeStart = breakTimeStart;
        this.breakTimeEnd = breakTimeEnd;
        this.introduce = introduce;
        this.deposit = deposit;
        this.reservationInterval = reservationInterval;
        this.owner = owner;
        this.address = address;
        this.location = location;
        initializePrice();
    }

    public void updateProfile(String name, String introduce, Integer deposit) {
        if (name != null) {
            this.name = name;
        }

        if (introduce != null) {
            this.introduce = introduce;
        }

        if (deposit != null) {
            this.deposit = deposit;
        }
    }

    public void updateMinMaxPrice() {
        Menus menus = new Menus(this.menus);

        this.minPrice = CalculatorUtil.ceilToNearestTenThousand(menus.getMinPrice());
        this.minPrice = CalculatorUtil.ceilToNearestTenThousand(menus.getMaxPrice());
    }

    public void updateMinMaxPrice(List<Menu> newMenus) {
        Menus menus = new Menus(newMenus);
        updateMinPrice(menus.getMinPrice());
        updateMaxPrice(menus.getMaxPrice());
    }

    public boolean isMinOrMaxPrice(int price) {
        price = CalculatorUtil.ceilToNearestTenThousand(price);

        return this.minPrice >= price || this.maxPrice <= price;
    }

    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
    }

    public void switchWaitingStatus() {
        if (this.waitingStatus == RestaurantWaitingStatus.OPEN) {
            this.waitingStatus = RestaurantWaitingStatus.CLOSE;
            return;
        }

        this.waitingStatus = RestaurantWaitingStatus.OPEN;
    }

    private void updateMinPrice(int minPrice) {
        minPrice = CalculatorUtil.ceilToNearestTenThousand(minPrice);

        if (this.minPrice > minPrice) {
            this.minPrice = minPrice;
        }
    }

    private void updateMaxPrice(int maxPrice) {
        maxPrice = CalculatorUtil.ceilToNearestTenThousand(maxPrice);

        if (this.maxPrice < maxPrice) {
            this.maxPrice = maxPrice;
        }
    }

    private void initializePrice() {
        this.maxPrice = 0;
        this.minPrice = Integer.MAX_VALUE;
    }
}
