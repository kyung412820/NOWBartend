package com.nowbartend.domain.customer.collection.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.nowbartend.domain.customer.collection.dto.response.CollectionInfoResponse;
import com.nowbartend.domain.customer.collection.dto.response.QCollectionDetailResponse;
import com.nowbartend.domain.customer.collection.dto.response.QCollectionInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.nowbartend.domain.customer.collection.entity.QCollection.collection;


@RequiredArgsConstructor
public class CollectionQRepositoryImpl implements CollectionQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CollectionDetailResponse> findCollectionDetail(Long collectionId) {
        return Optional.ofNullable(queryFactory
                .select(new QCollectionDetailResponse(
                        collection.id,
                        collection.user.id,
                        collection.name,
                        collection.description,
                        collection.isPublic,
                        collection.modifiedAt
                ))
                .from(collection)
                .where(collection.id.eq(collectionId))
                .fetchOne());
    }

    @Override
    public Page<CollectionInfoResponse> findAllByCollectionOwnerId(Long userId, Pageable pageable) {
        List<CollectionInfoResponse> content = queryFactory
                .select(new QCollectionInfoResponse(
                        collection.id,
                        collection.name,
                        collection.isPublic
                ))
                .from(collection)
                .where(collection.user.id.eq(userId))
                .orderBy(collection.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Integer> count = queryFactory
                .selectOne()
                .from(collection)
                .where(collection.user.id.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Page<CollectionInfoResponse> findAllByPublicCollections(Long userId, Pageable pageable) {
        List<CollectionInfoResponse> content = queryFactory
                .select(new QCollectionInfoResponse(
                        collection.id,
                        collection.name,
                        collection.isPublic
                ))
                .from(collection)
                .where(
                        collection.user.id.eq(userId)
                                .and(collection.isPublic.eq(true))
                )
                .orderBy(collection.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Integer> count = queryFactory
                .selectOne()
                .from(collection)
                .where(
                        collection.user.id.eq(userId)
                                .and(collection.isPublic.eq(true))
                );

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
