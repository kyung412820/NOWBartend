package com.nowbartend.domain.common.user.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserMyPageResponse(
        UserProfileResponse userProfile,
        long followerCount,
        long followingCount
) {

    public static UserMyPageResponse of(User user, long followerCount, long followingCount) {
        return UserMyPageResponse.builder()
                .userProfile(UserProfileResponse.from(user))
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
