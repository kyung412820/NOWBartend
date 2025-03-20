package com.nowbartend.domain.customer.waiting.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.waiting.dto.response.QWaitingInfoResponse;
import com.nowbartend.domain.customer.waiting.dto.response.WaitingInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.nowbartend.domain.owner.restaurant.entity.QRestaurant.restaurant;
import static com.nowbartend.domain.owner.restaurant.waiting.entity.QWaiting.waiting;

@RequiredArgsConstructor
public class WaitingQueryDslRepositoryImpl implements WaitingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WaitingInfoResponse> findWaitingInfo(Long userId, Pageable pageable) {
        List<WaitingInfoResponse> records = queryFactory
                .select(new QWaitingInfoResponse(
                        waiting.id,
                        waiting.user.id,
                        waiting.restaurant.id,
                        waiting.totalCount,
                        waiting.dailySequence,
                        waiting.status,
                        waiting.restaurant.name,
                        waiting.createdAt
                ))
                .from(waiting)
                .leftJoin(waiting.restaurant, restaurant)
                .where(waiting.user.id.eq(userId))
                .orderBy(waiting.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(waiting.count())
                .from(waiting)
                .leftJoin(waiting.restaurant, restaurant)
                .where(waiting.user.id.eq(userId));

        return PageableExecutionUtils.getPage(records, pageable, count::fetchOne);
    }
}
