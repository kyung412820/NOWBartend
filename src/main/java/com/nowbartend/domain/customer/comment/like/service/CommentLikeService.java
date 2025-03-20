package com.nowbartend.domain.customer.comment.like.service;

import com.nowbartend.domain.customer.comment.entity.Comment;
import com.nowbartend.domain.customer.comment.repository.CommentRepository;
import com.nowbartend.domain.customer.comment.like.entity.CommentLike;
import com.nowbartend.domain.customer.comment.like.repository.CommentLikeRepository;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    public void likeAndUnlikeComment(Long commentId, Long userId) {

        Comment comment = findCommentById(commentId);
        User user = findUserById(userId);

        if (commentLikeRepository.existsByCommentAndUser(comment, user)) {

            CommentLike commentLike = findCommentLikeByCommentAndUser(comment, user);

            commentLikeRepository.delete(commentLike);
        } else {

            CommentLike commentLike = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();

            commentLikeRepository.save(commentLike);
        }
    }

    private CommentLike findCommentLikeByCommentAndUser(Comment comment, User user) {
        return commentLikeRepository.findCommentLikeByCommentAndUser(comment, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.LIKE_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}