package com.nowbartend.domain.common.auth.dto.request;

public record AccessTokenReissueReq(
        String refreshToken
) {
}
