package com.nowbartend.domain.customer.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record CollectionDetailResponse(
        Long collectionId,
        Long userId,
        String name,
        String description,
        boolean isPublic,
        LocalDateTime modifiedAt
) {

    @QueryProjection
    public CollectionDetailResponse {
    }
}
