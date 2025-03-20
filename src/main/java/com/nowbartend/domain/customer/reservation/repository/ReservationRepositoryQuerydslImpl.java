package com.nowbartend.domain.customer.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.reservation.entity.Reservation;
import com.nowbartend.domain.customer.reservation.entity.ReservationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.nowbartend.domain.customer.reservation.entity.QReservation.reservation;

@RequiredArgsConstructor
public class ReservationRepositoryQuerydslImpl implements ReservationRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long countReservedReservationByRestaurantSeatAndDate(Long restaurantSeatId, LocalDateTime date) {
        return Optional.ofNullable(jpaQueryFactory.select(reservation.count())
                        .from(reservation)
                        .where(
                                reservation.restaurantSeat.id.eq(restaurantSeatId),
                                reservation.date.eq(date),
                                reservation.status.eq(ReservationStatus.RESERVED)
                        )
                        .fetchOne()
                )
                .orElse(0L);
    }

    @Override
    public Optional<Reservation> findReservedReservationById(Long reservationId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(reservation)
                        .where(
                                reservation.id.eq(reservationId),
                                reservation.status.eq(ReservationStatus.RESERVED)
                        )
                        .fetchOne()
        );
    }
}
