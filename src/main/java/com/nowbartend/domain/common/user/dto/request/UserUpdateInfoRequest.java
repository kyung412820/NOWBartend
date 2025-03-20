package com.nowbartend.domain.common.user.dto.request;

import static com.nowbartend.domain.common.auth.dto.AuthValidationMessage.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateInfoRequest(
        @Size(
                min = NAME_MIN,
                max = NAME_MAX,
                message = NAME_RANGE_MESSAGE
        )
        String name,

        @Pattern(
                regexp = PHONE_NUMBER_REG,
                message = INVALID_PHONE_NUMBER_MESSAGE
        )
        String phoneNumber
) {
}
