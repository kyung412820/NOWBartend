package com.nowbartend.domain.customer.restaurant.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.restaurant.dto.response.QRestaurantSimpleResponse;
import com.nowbartend.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.nowbartend.domain.customer.restaurant.model.RestaurantSearchDistance;
import com.nowbartend.domain.customer.restaurant.model.RestaurantSearchOrder;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.nowbartend.domain.admin.seatType.entity.QSeatType.seatType;
import static com.nowbartend.domain.customer.bookmark.entity.QBookmark.bookmark;
import static com.nowbartend.domain.owner.restaurant.entity.QRestaurant.restaurant;
import static com.nowbartend.domain.owner.restaurant.menus.entity.QMenu.menu;
import static com.nowbartend.domain.owner.restaurantseat.entity.QRestaurantSeat.restaurantSeat;

@RequiredArgsConstructor
public class CustomerRestaurantRepositoryQuerydslImpl implements CustomerRestaurantRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<RestaurantSimpleResponse> findRestaurantsByFilters(
            CustomUserDetails userDetails,
            Pageable pageable,
            String keyword,
            String address,
            Integer minPrice,
            Integer maxPrice,
            Long seatTypeId,
            Boolean isUseDistance,
            Double clientLat,
            Double clientLon,
            Integer distance,
            String order
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        Point clientLocation = null;

        if (Boolean.TRUE.equals(isUseDistance)) {
            RestaurantSearchDistance.contains(distance);
            clientLocation = GeometryUtil.createPoint(clientLat, clientLon);

            builder.and(containsExpression(clientLocation, distance));
        } else {
            if (StringUtils.hasText(address)) {
                builder.and(fullTextExpression(restaurant.address, address).gt(0));
            }
        }

        JPAQuery<RestaurantSimpleResponse> query = jpaQueryFactory.select(
                        new QRestaurantSimpleResponse(
                                restaurant.id,
                                restaurant.name,
                                restaurant.address,
                                restaurant.openTime,
                                restaurant.closeTime,
                                restaurant.minPrice,
                                restaurant.maxPrice,
                                isBookmarked(userDetails),
                                Boolean.TRUE.equals(isUseDistance) ?
                                        distanceSphereExpression(clientLocation) :
                                        nullExpression(Double.class),
                                restaurant.location
                        )
                )
                .from(restaurant);

        if (StringUtils.hasText(keyword)) {
            builder.and(
                    fullTextExpression(restaurant.name, keyword).gt(0)
//                            .or(fullTextExpression(menu.name, keyword).gt(0))
            );
//            query.leftJoin(restaurant.menus, menu);
        }

        if (minPrice != null && maxPrice != null) {
            builder.and(restaurant.minPrice.loe(maxPrice)).and(restaurant.maxPrice.goe(minPrice));
        }

        if (seatTypeId != null) {
            query.innerJoin(restaurantSeat).on(restaurantSeat.restaurant.eq(restaurant)
                    .and(seatType.id.eq(seatTypeId)));
        }

        OrderSpecifier<?>[] orderSpecifier = getOrderSpecifier(order, clientLocation, query);

        List<RestaurantSimpleResponse> records = query.where(builder)
                .groupBy(restaurant.id)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = records.size() > pageable.getPageSize();

        if (hasNext) {
            records.remove(records.size() - 1);
        }

        return new SliceImpl<>(records, pageable, hasNext);
    }

    private NumberTemplate<Double> distanceSphereExpression(Point clientLocation) {
        return numberTemplate(
                Double.class,
                "ST_DISTANCE_SPHERE({0}, {1})",
                clientLocation,
                restaurant.location
        );
    }

    private BooleanExpression containsExpression(Point clientLocation, Integer distance) {
        return booleanTemplate(
                "ST_CONTAINS(ST_BUFFER({0}, {1}), {2})",
                clientLocation,
                distance,
                restaurant.location
        );
    }

    private NumberTemplate<Double> fullTextExpression(StringPath name, String keyword) {
        return numberTemplate(
                Double.class,
                "match_against({0}, {1})",
                name,
                keyword);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(String order, Point clientLocation, JPAQuery<RestaurantSimpleResponse> query) {
        RestaurantSearchOrder restaurantOrder = RestaurantSearchOrder.from(order);

        return switch (restaurantOrder) {
            case NEARBY -> new OrderSpecifier<?>[]{distanceSphereExpression(clientLocation).asc()};
            case POPULARITY -> {
                query.leftJoin(bookmark).on(bookmark.restaurant.eq(restaurant));
                yield new OrderSpecifier<?>[]{bookmark.count().desc(), restaurant.id.desc()};
            }
        };
    }

    private BooleanExpression isBookmarked(CustomUserDetails userDetails) {
        return Optional.ofNullable(userDetails)
                .map(user -> JPAExpressions
                        .select(bookmark.count())
                        .from(bookmark)
                        .where(bookmark.restaurant.id.eq(restaurant.id)
                                .and(bookmark.user.id.eq(user.getId()))
                        )
                        .gt(0L))
                .orElse(null);
    }
}
