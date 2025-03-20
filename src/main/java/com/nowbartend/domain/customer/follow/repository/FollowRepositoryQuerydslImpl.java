package com.nowbartend.domain.customer.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.common.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.nowbartend.domain.customer.follow.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryQuerydslImpl implements FollowRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countFollowing(Long userId) {
        return Optional.ofNullable(queryFactory.select(follow.count())
                        .from(follow)
                        .where(follow.follower.id.eq(userId))
                        .fetchOne())
                .orElse(0L);
    }

    @Override
    public long countFollower(Long userId) {
        return Optional.ofNullable(queryFactory.select(follow.count())
                        .from(follow)
                        .where(follow.following.id.eq(userId))
                        .fetchOne())
                .orElse(0L);
    }

    @Override
    public List<User> findFollowersByFollowingId(Long userId, Long cursor, int limit) {
        return queryFactory.select(follow.follower)
                .from(follow)
                .where(follow.following.id.eq(userId)
                        .and(cursor != null ? follow.id.lt(cursor) : follow.id.isNotNull()))
                .orderBy(follow.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<User> findFollowingsByFollowerId(Long userId, Long cursor, int limit) {
        return queryFactory.select(follow.following)
                .from(follow)
                .where(follow.follower.id.eq(userId)
                        .and(cursor != null ? follow.id.lt(cursor) : follow.id.isNotNull()))
                .orderBy(follow.id.desc())
                .limit(limit)
                .fetch();
    }
}
