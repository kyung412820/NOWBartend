package com.nowbartend.domain.customer.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotNull(message = "게시글을 선택 해주세요")
        Long postId,

        @NotBlank(message = "공백을 입력할 수 없습니다.")
        @Size(max = 300, message = "최대 300자까지 입력 가능합니다.")
        String content,

        // 부모 댓글 아이디 (nullable)
        Long parentId
) {
}
