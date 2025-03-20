package com.nowbartend.domain.customer.collection.bookmark.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CollectionBookmarksFindResponse(
        List<CollectionBookmarkDetails> bookmarks,
        int curPage,
        int totalPages,
        long totalElements
) {

    public static CollectionBookmarksFindResponse from(Page<CollectionBookmarkDetails> bookmarkDetails) {
        return CollectionBookmarksFindResponse.builder()
                .bookmarks(bookmarkDetails.getContent())
                .curPage(bookmarkDetails.getNumber())
                .totalPages(bookmarkDetails.getTotalPages())
                .totalElements(bookmarkDetails.getTotalElements())
                .build();
    }
}
