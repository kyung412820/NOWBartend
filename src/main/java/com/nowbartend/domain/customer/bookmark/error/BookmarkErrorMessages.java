package com.nowbartend.domain.customer.bookmark.error;

import lombok.Getter;

@Getter
public class BookmarkErrorMessages {
    public static final String RESTAURANT_ID_SHOULD_NOT_BE_NULL = "RestaurantId 값이 NULL 이어서는 안됩니다.";
    public static final String MEMO_SHOULD_NOT_BE_NULL = "updateMemo 값이 NULL 이어서는 안됩니다.";
}
