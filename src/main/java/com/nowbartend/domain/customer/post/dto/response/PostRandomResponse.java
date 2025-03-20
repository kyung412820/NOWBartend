package com.nowbartend.domain.customer.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRandomResponse {

    private final Long postId;
    private final Long userId;
    private final Long regionId;
    private final String title;
    private final String content;
    private final String nickname;
    private final Integer commentCount;
    private final Integer likeCount;
    private final LocalDateTime modifiedAt;
    private final Long randomValue;

    @QueryProjection
    public PostRandomResponse(
            Long postId,
            Long userId,
            Long regionId,
            String title,
            String content,
            String nickname,
            Integer commentCount,
            Integer likeCount,
            LocalDateTime modifiedAt,
            Long randomValue
    ) {
        this.postId = postId;
        this.userId = userId;
        this.regionId = regionId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.modifiedAt = modifiedAt;
        this.randomValue = randomValue;
    }
}
