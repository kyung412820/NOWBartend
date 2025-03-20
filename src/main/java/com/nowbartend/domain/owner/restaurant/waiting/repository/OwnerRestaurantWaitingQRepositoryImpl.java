package com.nowbartend.domain.owner.restaurant.waiting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.owner.restaurant.waiting.dto.response.QWaitingDetails;
import com.nowbartend.domain.owner.restaurant.waiting.dto.response.WaitingDetails;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingStatus;
import com.nowbartend.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.nowbartend.domain.common.user.entity.QUser.user;
import static com.nowbartend.domain.owner.restaurant.waiting.entity.QWaiting.waiting;

@RequiredArgsConstructor
public class OwnerRestaurantWaitingQRepositoryImpl implements OwnerRestaurantWaitingQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WaitingDetails> findWaitingDetails(List<Long> userIds) {
        LocalDateTime startOfToday = DateTimeUtil.getStartOfToday();
        LocalDateTime endOfToday = DateTimeUtil.getEndOfToday();

        return queryFactory.select(
                        new QWaitingDetails(
                                waiting.id,
                                waiting.user.id,
                                waiting.user.name,
                                waiting.totalCount,
                                waiting.user.phoneNumber,
                                waiting.status,
                                waiting.type,
                                waiting.dailySequence
                        )
                )
                .from(waiting)
                .innerJoin(waiting.user, user)
                .where(
                        waiting.user.id.in(userIds),
                        waiting.status.in(WaitingStatus.REQUESTED, WaitingStatus.NOTIFIED),
                        waiting.createdAt.between(startOfToday, endOfToday)
                )
                .orderBy(waiting.createdAt.asc())
                .fetch();
    }
}
