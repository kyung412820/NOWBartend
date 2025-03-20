package com.nowbartend.domain.customer.post.dto.request;

import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        Long regionId,

        @Size(max = 20, message = "최대 20자까지 입력가능합니다.")
        String title,

        @Size(max = 300, message = "최대 300자까지 입력가능합니다.")
        String content
) {
}
