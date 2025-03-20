package com.nowbartend.domain.owner.restaurantseat.entity;

import com.nowbartend.domain.admin.seatType.entity.SeatType;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_seat_id")
    private Long id;

    @Column(nullable = false)
    private Integer minGuestCount;

    @Column(nullable = false)
    private Integer maxGuestCount;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public RestaurantSeat(
            Integer minGuestCount,
            Integer maxGuestCount,
            Integer quantity,
            SeatType seatType,
            Restaurant restaurant
    ) {
        this.minGuestCount = minGuestCount;
        this.maxGuestCount = maxGuestCount;
        this.quantity = quantity;
        this.seatType = seatType;
        this.restaurant = restaurant;
    }
}
