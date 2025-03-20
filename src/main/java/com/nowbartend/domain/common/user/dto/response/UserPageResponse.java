package com.nowbartend.domain.common.user.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserPageResponse(
        Long userId,
        UserProfileResponse userProfile,
        long followerCount,
        long followingCount
) {

    public static UserPageResponse of(User user, long followerCount, long followingCount) {
        return UserPageResponse.builder()
                .userId(user.getId())
                .userProfile(UserProfileResponse.from(user))
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}

