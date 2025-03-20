package com.nowbartend.domain.common.notification.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.common.notification.entity.Notification;
import com.nowbartend.domain.common.notification.entity.QNotification;
import com.nowbartend.domain.customer.reservation.entity.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.nowbartend.domain.common.notification.entity.QNotification.notification;
import static com.nowbartend.domain.common.user.entity.QUser.user;
import static com.nowbartend.domain.customer.reservation.entity.QReservation.reservation;
import static com.nowbartend.domain.owner.restaurant.entity.QRestaurant.restaurant;
import static com.nowbartend.domain.owner.restaurantseat.entity.QRestaurantSeat.restaurantSeat;

@RequiredArgsConstructor
public class NotificationRepositoryQuerydslImpl implements NotificationRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    // 알림 미전송 된 것 중 보내야 하는 알림 조회
    @Override
    public Page<Notification> findDueNotifications(LocalDateTime currentTime, Pageable pageable) {

        // fetch join 쿼리로 필요한 연관 데이터 미리 로딩
        JPAQuery<Notification> query = queryFactory
                .selectFrom(notification)
                .join(notification.reservation, reservation).fetchJoin()
                .join(reservation.restaurantSeat, restaurantSeat).fetchJoin()
                .join(restaurantSeat.restaurant, restaurant).fetchJoin()
                .join(notification.user, user).fetchJoin()
                .where(notification.isSent.eq(false)
                        .and(notification.scheduledTime.loe(currentTime))
                        .and(notification.reservation.status.eq(ReservationStatus.RESERVED)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Notification> notifications = query.fetch();

        Long total = queryFactory
                .select(notification.count())
                .from(notification)
                .where(notification.isSent.eq(false)
                        .and(notification.scheduledTime.loe(currentTime)))
                .fetchOne();

        return new PageImpl<>(notifications, pageable, total);
    }

    // 알림의 전송 상태를 변경하는 update 쿼리, isSent가 false인 경우에만 업데이트
    @Override
    public int updateIsSentTrue(Long id) {

        QNotification notification = QNotification.notification;

        long changedRowCount = queryFactory
                .update(notification)
                .set(notification.isSent, true)
                .where(notification.id.eq(id)
                        .and(notification.isSent.eq(false))
                        .and(notification.reservation.status.eq(ReservationStatus.RESERVED)))
                .execute();

        return (int) changedRowCount;
    }
}
