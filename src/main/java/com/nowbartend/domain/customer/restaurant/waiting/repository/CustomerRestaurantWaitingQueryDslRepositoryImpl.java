package com.nowbartend.domain.customer.restaurant.waiting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingType;
import com.nowbartend.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.nowbartend.domain.owner.restaurant.waiting.entity.QWaiting.waiting;

@RequiredArgsConstructor
public class CustomerRestaurantWaitingQueryDslRepositoryImpl implements CustomerRestaurantWaitingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Integer> findTodayLastSequence(Long restaurantId, WaitingType waitingType) {
        LocalDateTime startOfToday = DateTimeUtil.getStartOfToday();
        LocalDateTime endOfToday = DateTimeUtil.getEndOfToday();

        return Optional.ofNullable(queryFactory
                .select(
                        waiting.dailySequence
                )
                .from(waiting)
                .where(
                        waiting.restaurant.id.eq(restaurantId),
                        waiting.createdAt.between(startOfToday, endOfToday),
                        waiting.type.eq(waitingType)
                )
                .orderBy(waiting.createdAt.desc())
                .fetchFirst());
    }
}
