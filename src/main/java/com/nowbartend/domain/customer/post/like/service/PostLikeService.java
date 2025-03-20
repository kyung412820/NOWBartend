package com.nowbartend.domain.customer.post.like.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.post.entity.Post;
import com.nowbartend.domain.customer.post.like.entity.PostLike;
import com.nowbartend.domain.customer.post.like.repository.PostLikeRepository;
import com.nowbartend.domain.customer.post.repository.PostRepository;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public void likeAndUnlikePost(Long postId, Long userId) {
        Post post = findPostById(postId);
        User user = findUserById(userId);

        if (postLikeRepository.existsByPostAndUser(post, user)) {

            PostLike postLike = findPostLikeByPostAndUser(post, user);

            postLikeRepository.delete(postLike);
        } else {

            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();

            post.addLike(postLike);

            postLikeRepository.save(postLike);
        }
    }

    private PostLike findPostLikeByPostAndUser(Post post, User user) {
        return postLikeRepository.findPostLikeByPostAndUser(post, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.LIKE_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Post findPostById(Long commentId) {
        return postRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }
}
