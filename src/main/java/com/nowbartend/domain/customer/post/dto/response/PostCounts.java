package com.nowbartend.domain.customer.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record PostCounts (
        Long postId,
        Integer commentCount,
        Integer likeCount
) {
    @QueryProjection
    public PostCounts{

    }
}
