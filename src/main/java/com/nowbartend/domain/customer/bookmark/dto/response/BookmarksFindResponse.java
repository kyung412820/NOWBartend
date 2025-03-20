package com.nowbartend.domain.customer.bookmark.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record BookmarksFindResponse(
        List<BookmarkDetails> bookmarks,
        int curPage,
        int totalPages,
        long totalElements
) {

    public static BookmarksFindResponse from(Page<BookmarkDetails> bookmarkDetails) {
        return BookmarksFindResponse.builder()
                .bookmarks(bookmarkDetails.getContent())
                .curPage(bookmarkDetails.getNumber())
                .totalPages(bookmarkDetails.getTotalPages())
                .totalElements(bookmarkDetails.getTotalElements())
                .build();
    }
}
