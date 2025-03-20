package com.nowbartend.domain.customer.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @NotBlank(message = "공백으로 수정할 수 없습니다.")
        @Size(max = 300, message = "최대 300자까지 입력 가능합니다.")
        String content
) {
}
