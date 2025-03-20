package com.nowbartend.domain.common.user.entity;

import com.nowbartend.global.exception.InvalidException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    CUSTOMER("ROLE_CUSTOMER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.getValue().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID_USER_ROLE));
    }
}
