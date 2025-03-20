package com.nowbartend.domain.customer.follow.dto;

public class FollowValidationMessage {
    public static final String FOLLOWING_ID_POSITIVE = "Following ID는 양수여야 합니다.";
    public static final String USER_ID_POSITIVE = "User ID는 양수여야 합니다.";
    public static final String LIMIT_POSITIVE = "Limit은 양수여야 합니다.";
    public static final String USER_ID_NOT_NULL = "User ID는 null일 수 없습니다.";
    public static final String NAME_NOT_BLANK = "이름은 비어 있을 수 없습니다.";
}