package com.nowbartend.domain.customer.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.nowbartend.domain.customer.post.entity.PostDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PostKeywordResponse {

    private final Long postId;
    private final Long userId;
    private final Long regionId;
    private final String title;
    private final String content;
    private final String nickname;
    private final Integer commentCount;
    private final Integer likeCount;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public PostKeywordResponse(
            Long postId,
            Long userId,
            Long regionId,
            String title,
            String content,
            String nickname,
            Integer commentCount,
            Integer likeCount,
            LocalDateTime modifiedAt
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
    }

    public static PostKeywordResponse from(PostDocument post, Integer commentCount, Integer likeCount){

        LocalDateTime localDateTime = LocalDateTime.ofInstant(post.getModifiedAt().toInstant(), ZoneId.systemDefault());

        return PostKeywordResponse.builder()
                .postId(Long.valueOf(post.getId()))
                .userId(post.getUserId())
                .nickname(post.getNickname())
                .regionId(post.getRegionId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .modifiedAt(localDateTime)
                .build();
    }
}
