package com.nowbartend.domain.customer.comment.like.repository;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.comment.entity.Comment;
import com.nowbartend.domain.customer.comment.like.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndUser(Comment comment, User user);

    Optional<CommentLike> findCommentLikeByCommentAndUser(Comment comment, User user);
}
