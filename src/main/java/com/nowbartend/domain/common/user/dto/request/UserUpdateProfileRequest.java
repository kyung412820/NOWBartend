package com.nowbartend.domain.common.user.dto.request;

import static com.nowbartend.domain.common.auth.dto.AuthValidationMessage.*;

import jakarta.validation.constraints.Size;

public record UserUpdateProfileRequest(
        @Size(
                min = NICKNAME_MIN,
                max = NICKNAME_MAX,
                message = NICKNAME_RANGE_MESSAGE
        )
        String nickname,

        @Size(
                max = INTRODUCE_MAX,
                message = INTRODUCE_RANGE_MESSAGE
        )
        String introduce,
        Long regionId,
        Boolean isChangeRegion
) {
}
