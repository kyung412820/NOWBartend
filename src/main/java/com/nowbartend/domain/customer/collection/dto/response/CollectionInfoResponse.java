package com.nowbartend.domain.customer.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record CollectionInfoResponse(
        Long collectionId,
        String name,
        boolean isPublic
) {

    @QueryProjection
    public CollectionInfoResponse {
    }
}
