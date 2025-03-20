package com.nowbartend.domain.common.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AuthLoginResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiryTime
) {

    public static AuthLoginResponse of(String accessToken, String refreshToken, long accessTokenExpiryTime) {
        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiryTime(accessTokenExpiryTime)
                .build();
    }
}
