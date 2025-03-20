package com.nowbartend.domain.customer.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record BookmarkDetails(
        Long bookmarkId,
        Long restaurantId,
        String bookmarkMemo,
        String restaurantName,
        String restaurantIntroduction,
        String restaurantAddress
) {

    @QueryProjection
    public BookmarkDetails {
    }
}
