package com.nowbartend.domain.customer.post.like.repository;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.post.entity.Post;
import com.nowbartend.domain.customer.post.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndUser(Post post, User user);

    Optional<PostLike> findPostLikeByPostAndUser(Post post, User user);
}
