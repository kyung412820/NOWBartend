package com.nowbartend.domain.customer.collection.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CollectionInfosResponse(
        List<CollectionInfoResponse> collectionInfos,
        int curPage,
        int totalPages,
        long totalElements
) {

    public static CollectionInfosResponse from(Page<CollectionInfoResponse> collectionInfos) {
        return CollectionInfosResponse.builder()
                .collectionInfos(collectionInfos.getContent())
                .curPage(collectionInfos.getNumber())
                .totalPages(collectionInfos.getTotalPages())
                .totalElements(collectionInfos.getTotalElements())
                .build();
    }
}
