package com.nowbartend.domain.customer.comment.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentRepliesResponse {

    private final Long commentId;
    private final Long postId;
    private final Long userId;
    private final String username;
    private final String content;
    private final Integer likeCount;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public CommentRepliesResponse(
            Long commentId,
            Long postId,
            Long userId,
            String username,
            String content,
            Integer likeCount,
            LocalDateTime modifiedAt
    ) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.likeCount = likeCount;
        this.modifiedAt = modifiedAt;
    }
}
