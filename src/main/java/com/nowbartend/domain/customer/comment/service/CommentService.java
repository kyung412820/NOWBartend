package com.nowbartend.domain.customer.comment.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.comment.dto.request.CommentCreateRequest;
import com.nowbartend.domain.customer.comment.dto.request.CommentUpdateRequest;
import com.nowbartend.domain.customer.comment.dto.response.CommentPostResponse;
import com.nowbartend.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.nowbartend.domain.customer.comment.entity.Comment;
import com.nowbartend.domain.customer.comment.repository.CommentRepository;
import com.nowbartend.domain.customer.post.entity.Post;
import com.nowbartend.domain.customer.post.repository.PostRepository;
import com.nowbartend.global.exception.ForbiddenException;
import com.nowbartend.global.exception.InvalidException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(Long userId, CommentCreateRequest request) {
        Post post = findPostById(request.postId());
        User user = findUserById(userId);

        // 부분적으로 빌드 후 게시글 댓글인지 대댓글인지 구분 후 조건에 따라 완성
        Comment.CommentBuilder builder = Comment.builder()
                .post(post)
                .content(request.content())
                .user(user);

        if (request.parentId() != null) {
            // 대댓글 : 부모 댓글 조회 후 설정
            Comment parentComment = findCommentById(request.parentId());

            if (!post.getId().equals(parentComment.getPost().getId())) {
                throw new InvalidException(ErrorCode.POST_NOT_EQUAL);
            }

            Comment comment = builder.parent(parentComment)
                    .build();

            // 댓글의 대댓글 리스트에만 추가
            parentComment.addReply(comment);

            commentRepository.save(comment);

            return comment.getId();

        } else {
            // 게시글 댓글
            Comment comment = builder.build();

            // 게시글의 댓글리스트에 추가
            post.addComment(comment);

            commentRepository.save(comment);

            return comment.getId();
        }
    }

    public Slice<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit) {
        List<CommentPostResponse> replies = commentRepository.findPostComments(
                postId,
                cursorValue,
                limit);

        boolean hasNext = replies.size() == limit;

        return new SliceImpl<>(replies, PageRequest.of(0, limit), hasNext);
    }

    public Page<CommentRepliesResponse> findReplies(Long parentCommentId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1 , size);

        return commentRepository.findReplies(
                parentCommentId,
                pageable
        );
    }

    @Transactional
    public void update(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = findCommentById(commentId);

        isCommentOwner(comment.getUser().getId(), userId);

        comment.update(request.content());
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = findCommentById(commentId);

        isCommentOwner(comment.getUser().getId(), userId);

        commentRepository.delete(comment);
    }

    // 헬퍼 메서드

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void isCommentOwner(Long commentUserId, Long userId) {
        if (!Objects.equals(commentUserId, userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
