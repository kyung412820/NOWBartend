package com.nowbartend.domain.customer.collection.bookmark.dto.request;

import jakarta.validation.Valid;

import java.util.List;

public record CollectionBookmarksCreateRequest(
        @Valid
        List<CollectionBookmarkCreateRequest> bookmarks
) {
}
