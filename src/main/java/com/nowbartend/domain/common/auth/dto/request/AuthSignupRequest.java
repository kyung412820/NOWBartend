package com.nowbartend.domain.common.auth.dto.request;

import static com.nowbartend.domain.common.auth.dto.AuthValidationMessage.*;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthSignupRequest(
        @NotBlank(message = EMAIL_BLANK_MESSAGE)
        @Pattern(
                regexp = EMAIL_REG,
                message = INVALID_EMAIL_MESSAGE
        )
        String email,

        @NotBlank(message = PASSWORD_BLANK_MESSAGE)
        @Size(
                min = PASSWORD_MIN,
                max = PASSWORD_MAX,
                message = PASSWORD_RANGE_MESSAGE)
        @Pattern(
                regexp = PASSWORD_REG,
                message = INVALID_PASSWORD_MESSAGE
        )
        String password,

        @NotBlank(message = NAME_BLANK_MESSAGE)
        @Size(
                min = NAME_MIN,
                max = NAME_MAX,
                message = NAME_RANGE_MESSAGE
        )
        String name,

        @NotBlank(message = NICKNAME_BLANK_MESSAGE)
        @Size(
                min = NICKNAME_MIN,
                max = NICKNAME_MAX,
                message = NICKNAME_RANGE_MESSAGE
        )
        String nickname,

        @NotBlank(message = PHONE_NUMBER_BLANK_MESSAGE)
        @Pattern(
                regexp = PHONE_NUMBER_REG,
                message = INVALID_PHONE_NUMBER_MESSAGE
        )
        String phoneNumber,

        @NotNull(message = ROLE_NULL_MESSAGE)
        UserRole userRole
) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role(userRole)
                .build();
    }
}