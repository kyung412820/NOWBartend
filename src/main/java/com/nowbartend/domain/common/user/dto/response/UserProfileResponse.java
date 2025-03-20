package com.nowbartend.domain.common.user.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserProfileResponse(
        String userNickname,
        String userIntroduce,
        String regionName
) {

    public static UserProfileResponse from(User user) {
        String regionName = user.getRegion() != null ? user.getRegion().getName() : null;

        return UserProfileResponse.builder()
                .userNickname(user.getNickname())
                .userIntroduce(user.getIntroduce())
                .regionName(regionName)
                .build();
    }
}
