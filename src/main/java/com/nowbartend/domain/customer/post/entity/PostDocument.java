package com.nowbartend.domain.customer.post.entity;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.post.region.entity.Region;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Getter
@Document(indexName = "posts")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDocument {

    @Id
    private String id;

    @Field(name = "post_id", type = FieldType.Long)
    private Long postId;

    @Field(name = "user_id", type = FieldType.Long)
    private Long userId;

    @Field(name = "region_id", type = FieldType.Long)
    private Long regionId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(name = "region_name", type = FieldType.Text)
    private String regionName;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Text)
    private String content;

    @Field(name = "modified_at", type = FieldType.Date, format = DateFormat.strict_date_optional_time_nanos)
    private OffsetDateTime modifiedAt;

    public static PostDocument from(Post post, Region region, User user) {

        String id = String.valueOf(post.getId());

        // LocalDateTime을 밀리초 단위로 잘라내고 시스템 기본 타임존을 적용하여 OffsetDateTime으로 변환
        LocalDateTime modifiedLocal = post.getModifiedAt().truncatedTo(ChronoUnit.MILLIS);
        OffsetDateTime offsetModified = modifiedLocal.atZone(ZoneId.systemDefault()).toOffsetDateTime();

        return new PostDocument(
                id,
                post.getId(),
                user.getId(),
                region.getId(),
                post.getTitle(),
                region.getName(),
                user.getNickname(),
                post.getContent(),
                offsetModified
        );
    }
}
