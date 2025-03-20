package com.nowbartend.domain.customer.follow.dto.response;

import com.nowbartend.domain.common.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

import static com.nowbartend.domain.customer.follow.dto.FollowValidationMessage.NAME_NOT_BLANK;
import static com.nowbartend.domain.customer.follow.dto.FollowValidationMessage.USER_ID_NOT_NULL;

@Builder(access = AccessLevel.PRIVATE)
public record FollowUserResponse(
        @NotNull(message = USER_ID_NOT_NULL)
        Long userId,

        @NotBlank(message = NAME_NOT_BLANK)
        String name
) {

    public static FollowUserResponse from(User user) {
        return FollowUserResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }
}