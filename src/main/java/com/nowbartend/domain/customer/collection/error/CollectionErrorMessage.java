package com.nowbartend.domain.customer.collection.error;

import lombok.Getter;

@Getter
public class CollectionErrorMessage {

    public static final String NAME_SHOULD_NOT_BE_NULL_OR_EMPTY = "name 값이 NULL 또는 빈 문자열 이어서는 안됩니다..";
    public static final String DESCRIPTION_SHOULD_NOT_BE_NULL = "description 값이 NULL 이어서는 안됩니다.";
    public static final String IS_PUBLIC_SHOULD_NOT_BE_NULL = "isPublic 값이 NULL 이어서는 안됩니다.";
}
