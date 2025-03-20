package com.nowbartend.domain.customer.bookmark.dto.request;

import com.nowbartend.domain.customer.bookmark.error.BookmarkErrorMessages;
import jakarta.validation.constraints.NotNull;

public record BookmarkUpdateRequest(
        @NotNull(message = BookmarkErrorMessages.MEMO_SHOULD_NOT_BE_NULL)
        String updateMemo
) {
}
