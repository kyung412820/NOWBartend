package com.nowbartend.domain.customer.collection.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record CollectionBookmarkDetails(
        Long collectionBookmarkId,
        Long restaurantId,
        String bookmarkMemo,
        String restaurantName,
        String restaurantIntroduction,
        String restaurantAddress,
        Double longitude,
        Double latitude
) {

    @QueryProjection
    public CollectionBookmarkDetails {
    }
}
