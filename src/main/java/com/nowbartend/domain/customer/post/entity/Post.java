package com.nowbartend.domain.customer.post.entity;

import com.nowbartend.domain.common.BaseEntity;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.comment.entity.Comment;
import com.nowbartend.domain.customer.post.like.entity.PostLike;
import com.nowbartend.domain.customer.post.region.entity.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostLike> likes = new ArrayList<>();

    @Builder
    public Post(
            String title,
            String content,
            Region region,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.user = user;
    }

    // 편의성 메서드
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void addLike(PostLike postLike) {
        likes.add(postLike);
        postLike.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void update(String title, String content, Region region) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }

        if (content != null && !content.isEmpty()) {
            this.content = content;
        }

        if (region != null) {
            this.region = region;
        }
    }
}
