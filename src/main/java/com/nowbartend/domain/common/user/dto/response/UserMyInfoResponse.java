package com.nowbartend.domain.common.user.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserMyInfoResponse(
        String name,
        String phoneNumber
) {

    public static UserMyInfoResponse from(User user) {
        return UserMyInfoResponse.builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
