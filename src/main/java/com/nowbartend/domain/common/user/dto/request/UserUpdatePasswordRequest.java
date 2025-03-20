package com.nowbartend.domain.common.user.dto.request;

import static com.nowbartend.domain.common.auth.dto.AuthValidationMessage.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequest(
        @NotBlank(message = PASSWORD_BLANK_MESSAGE)
        @Size(
                min = PASSWORD_MIN,
                max = PASSWORD_MAX,
                message = PASSWORD_RANGE_MESSAGE)
        @Pattern(
                regexp = PASSWORD_REG,
                message = INVALID_PASSWORD_MESSAGE
        )
        String password
) {
}
