package com.nowbartend.domain.customer.post.repository;

import com.nowbartend.domain.customer.post.dto.response.PostRandomResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.follow.entity.QFollow;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.nowbartend.domain.customer.comment.entity.QComment.comment;
import static com.nowbartend.domain.customer.post.entity.QPost.post;
import static com.nowbartend.domain.customer.post.like.entity.QPostLike.postLike;

@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostRandomResponse> findPostsByRandom(
            Long userId,
            Long regionId,
            Long cursorValue,
            int limit,
            int randomSeed
    ) {

        BooleanBuilder builder = new BooleanBuilder();

        // 지역값 null이면 전체, 있으면 특정 지역 검색
        if (regionId != null) {
            builder.and(post.region.id.eq(regionId));
        }

        // 각 행마다 post.id와 randomSeed를 조합하여 해시함수로 랜덤값 도출                                                                                  계산
        NumberExpression<Long> randomValue = Expressions.numberTemplate(Long.class,
                "MOD(CRC32(CONCAT({0}, {1})), {2})", post.id, randomSeed, 1000000000);

        // 만약 cursorValue 가 있으면 그 값보다 큰 행들 조회
        if (cursorValue != null) {
            builder.and(randomValue.gt(cursorValue));
        }

        QFollow follow = QFollow.follow;

         JPQLQuery<Long> followedUserSubQuery = JPAExpressions
                .select(follow.following.id)
                .from(follow)
                .where(follow.follower.id.eq(userId));

        // 팔로우한 사용자의 게시글은 0, 그 외는 1을 부여
        NumberExpression<Integer> followOrder = new CaseBuilder()
                .when(post.user.id.in(followedUserSubQuery)).then(0)
                .otherwise(1);

        return queryFactory
                .select(
                        new QPostRandomResponse(
                                post.id,
                                post.user.id,
                                post.region.id,
                                post.title,
                                post.content,
                                post.user.name,
                                JPAExpressions
                                        .select(comment.count().coalesce(0L).intValue())
                                        .from(comment)
                                        .where(comment.post.id.eq(post.id)),
                                JPAExpressions
                                        .select(postLike.count().coalesce(0L).intValue())
                                        .from(postLike)
                                        .where(postLike.post.id.eq(post.id)),
                                post.modifiedAt,
                                randomValue
                        )
                )
                .from(post)
                .where(builder)
                // 계산한 랜덤 값들을 기준으로 오름차순으로 정렬 (만약 중복된다면 id 오름차순으로)
                .orderBy(followOrder.asc(), randomValue.asc(), post.id.asc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<PostKeywordResponse> findPostsByKeyword(
            Long userId,
            Long regionId,
            Long cursorValue,
            String keyword,
            int limit
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (regionId != null) {
            builder.and(post.region.id.eq(regionId));
        }

        // 최신순으로 정렬해서 커서값(게시글 ID)보다 낮은 행들을 조회
        if (cursorValue != null) {
            builder.and(post.id.lt(cursorValue));
        }

        BooleanBuilder keywordBuilder = new BooleanBuilder();

        // 키워드 검색
        keywordBuilder.or(post.title.containsIgnoreCase(keyword))
                .or(post.content.containsIgnoreCase(keyword))
                .or(post.user.name.containsIgnoreCase(keyword));

        builder.and(keywordBuilder);

        return queryFactory
                .select(
                        new QPostKeywordResponse(
                                post.id,
                                post.user.id,
                                post.region.id,
                                post.title,
                                post.content,
                                post.user.name,
                                JPAExpressions
                                        .select(comment.count().coalesce(0L).intValue())
                                        .from(comment)
                                        .where(comment.post.id.eq(post.id)),
                                JPAExpressions
                                        .select(postLike.count().coalesce(0L).intValue())
                                        .from(postLike)
                                        .where(postLike.post.id.eq(post.id)),
                                post.modifiedAt
                        )
                )
                .from(post)
                .where(builder)
                // 내림차순(최신순) 정렬
                .orderBy(post.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<PostCounts> findAllByIdWithCommentsAndLikes(List<Long> postIds) {

        return queryFactory.select(
                        new QPostCounts(post.id,
                                JPAExpressions.select(comment.count().intValue())
                                        .from(comment)
                                        .where(comment.post.eq(post)),
                                JPAExpressions.select(postLike.count().intValue())
                                        .from(postLike)
                                        .where(postLike.post.eq(post)))
                )
                .from(post)
                .where(post.id.in(postIds))
                .fetch();
    }
}
