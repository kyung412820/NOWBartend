package com.nowbartend.domain.customer.comment.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentPostResponse {

    private final Long commentId;
    private final Long postId;
    private final Long userId;
    private final String username;
    private final String content;
    private final Integer replyCount;
    private final Integer likeCount;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public CommentPostResponse(
            Long commentId,
            Long postId,
            Long userId,
            String username,
            String content,
            Integer replyCount,
            Integer likeCount,
            LocalDateTime modifiedAt
    ) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.modifiedAt = modifiedAt;
    }
}
