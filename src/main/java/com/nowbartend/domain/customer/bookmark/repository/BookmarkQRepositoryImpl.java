package com.nowbartend.domain.customer.bookmark.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.nowbartend.domain.customer.bookmark.dto.response.QBookmarkDetails;
import com.nowbartend.domain.customer.bookmark.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.nowbartend.domain.customer.bookmark.entity.QBookmark.bookmark;
import static com.nowbartend.domain.customer.collection.bookmark.entity.QCollectionBookmark.collectionBookmark;
import static com.nowbartend.domain.owner.restaurant.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
public class BookmarkQRepositoryImpl implements BookmarkQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        Integer result = queryFactory
                .selectOne()
                .from(bookmark)
                .where(
                        bookmark.user.id.eq(userId),
                        bookmark.restaurant.id.eq(restaurantId)
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<Bookmark> findAllByBookmarkIds(List<Long> bookmarkIds) {
        return queryFactory
                .selectFrom(bookmark)
                .where(bookmark.id.in(bookmarkIds))
                .fetch();
    }

    @Override
    public Page<BookmarkDetails> findBookmarkDetailsPaginationByUserId(Long userId, Pageable pageable) {
        List<BookmarkDetails> records = queryFactory
                .select(new QBookmarkDetails(
                        bookmark.id,
                        bookmark.restaurant.id,
                        bookmark.memo,
                        restaurant.name,
                        restaurant.introduce,
                        restaurant.address
                ))
                .from(bookmark)
                .innerJoin(bookmark.restaurant, restaurant)
                .where(bookmark.user.id.eq(userId))
                .orderBy(bookmark.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(bookmark.count())
                .from(bookmark)
                .where(bookmark.user.id.eq(userId));

        return PageableExecutionUtils.getPage(records, pageable, count::fetchOne);
    }

    @Override
    public Page<BookmarkDetails> findBookmarkDetailsByUserIdAndNotInCollection(Long userId, Long collectionId, Pageable pageable) {
        List<BookmarkDetails> records = queryFactory
                .select(new QBookmarkDetails(
                        bookmark.id,
                        bookmark.restaurant.id,
                        bookmark.memo,
                        restaurant.name,
                        restaurant.introduce,
                        restaurant.address
                ))
                .from(bookmark)
                .innerJoin(bookmark.restaurant, restaurant)
                .leftJoin(collectionBookmark)
                .on(collectionBookmark.bookmark.id.eq(bookmark.id)
                        .and(collectionBookmark.collection.id.eq(collectionId))
                )
                .where(
                        bookmark.user.id.eq(userId)
                                .and(collectionBookmark.bookmark.id.isNull())
                )
                .orderBy(bookmark.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // TODO: count 쿼리 최적화
        JPAQuery<Long> count = queryFactory
                .select(bookmark.count())
                .from(bookmark)
                .leftJoin(collectionBookmark)
                .on(collectionBookmark.bookmark.id.eq(bookmark.id)
                        .and(collectionBookmark.collection.id.eq(collectionId))
                )
                .where(
                        bookmark.user.id.eq(userId)
                                .and(collectionBookmark.bookmark.id.isNull())
                );

        return PageableExecutionUtils.getPage(records, pageable, count::fetchOne);
    }
}
