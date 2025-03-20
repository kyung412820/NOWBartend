package com.nowbartend.domain.customer.comment.repository;

import com.nowbartend.domain.customer.comment.dto.response.CommentPostResponse;
import com.nowbartend.domain.customer.comment.dto.response.CommentRepliesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryQuerydsl {

    List<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit);

    Page<CommentRepliesResponse> findReplies(Long parentCommentId, Pageable pageable);
}
