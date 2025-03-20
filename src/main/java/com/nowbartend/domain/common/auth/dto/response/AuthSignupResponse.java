package com.nowbartend.domain.common.auth.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AuthSignupResponse(
        String email,
        String name
) {

    public static AuthSignupResponse from(User user) {
        return AuthSignupResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
