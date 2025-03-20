package com.nowbartend.domain.customer.comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.nowbartend.domain.customer.comment.dto.response.CommentPostResponse;
import com.nowbartend.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.nowbartend.domain.customer.comment.dto.response.QCommentPostResponse;
import com.nowbartend.domain.customer.comment.dto.response.QCommentRepliesResponse;
import com.nowbartend.domain.customer.comment.entity.QComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nowbartend.domain.customer.comment.entity.QComment.comment;
import static com.nowbartend.domain.customer.comment.like.entity.QCommentLike.commentLike;

@RequiredArgsConstructor
public class CommentRepositoryQuerydslImpl implements CommentRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit) {

        QComment reply = new QComment("reply");

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.post.id.eq(postId))
                .and(comment.parent.isNull());

        if (cursorValue != null) {
            builder.and(comment.id.lt(cursorValue));
        }

        return queryFactory
                .select(
                        new QCommentPostResponse(
                                comment.id,
                                comment.post.id,
                                comment.user.id,
                                comment.user.name,
                                comment.content,
                                JPAExpressions
                                        .select(reply.count().coalesce(0L).intValue())
                                        .from(reply)
                                        .where(reply.parent.id.eq(comment.id)),
                                JPAExpressions
                                        .select(commentLike.count().coalesce(0L).intValue())
                                        .from(commentLike)
                                        .where(commentLike.comment.id.eq(comment.id)),
                                comment.modifiedAt
                        )
                )
                .from(comment)
                .where(builder)
                .orderBy(comment.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public Page<CommentRepliesResponse> findReplies(Long parentCommentId, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.parent.id.eq(parentCommentId));

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(builder)
                .fetchOne();

        List<CommentRepliesResponse> result = queryFactory
                .select(
                        new QCommentRepliesResponse(
                                comment.id,
                                comment.post.id,
                                comment.user.id,
                                comment.user.name,
                                comment.content,
                                JPAExpressions
                                        .select(commentLike.count().coalesce(0L).intValue())
                                        .from(commentLike)
                                        .where(commentLike.comment.id.eq(comment.id)),
                                comment.modifiedAt
                        )
                )
                .from(comment)
                .where(builder)
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, total);
    }
}
