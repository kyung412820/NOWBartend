package com.nowbartend.domain.customer.post.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.post.dto.request.PostCreateRequest;
import com.nowbartend.domain.customer.post.dto.request.PostUpdateRequest;
import com.nowbartend.domain.customer.post.dto.response.PostKeywordResponse;
import com.nowbartend.domain.customer.post.dto.response. PostRandomResponse;
import com.nowbartend.domain.customer.post.entity.Post;
import com.nowbartend.domain.customer.post.entity.PostDocument;
import com.nowbartend.domain.customer.post.region.entity.Region;
import com.nowbartend.domain.customer.post.region.repository.RegionRepository;
import com.nowbartend.domain.customer.post.repository.PostElasticRepository;
import com.nowbartend.domain.customer.post.repository.PostRepository;
import com.nowbartend.global.exception.ForbiddenException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostElasticRepository postElasticRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;


    @Transactional
    public Long createPost(Long userId, PostCreateRequest request) {
        User user = findUserById(userId);
        Region region = findRegionById(request.regionId());

        Post post = request.toEntity(user, region);

        postRepository.save(post);

        PostDocument postDocument = PostDocument.from(post, region, user);

        postElasticRepository.save(postDocument);

        return post.getId();
    }

    public Slice<PostRandomResponse> findPostsByRandom(
            Long userId,
            Long regionId,
            Long cursorValue,
            int limit,
            int randomSeed
    ) {
        List<PostRandomResponse> posts = postRepository.findPostsByRandom(
                userId,
                regionId,
                cursorValue,
                limit + 1,
                randomSeed);

        // 10개를 조회할 때 11개를 조회가 된다면 다음페이지가 있다고 판단
        boolean hasNext = posts.size() == limit;

        if (hasNext) {
            posts = posts.subList(0, limit);
        }

        return new SliceImpl<>(posts, PageRequest.of(0, limit), hasNext);
    }

    public Slice<PostKeywordResponse> findPostsByKeyword(
            Long userId,
            Long regionId,
            Long cursorValue,
            String keyword,
            int limit
    ) {
        List<PostKeywordResponse> posts = postRepository.findPostsByKeyword(
                userId,
                regionId,
                cursorValue,
                keyword,
                limit + 1);

        // 10개를 조회할 때 11개를 조회가 된다면 다음페이지가 있다고 판단
        boolean hasNext = posts.size() == limit + 1;

        if (hasNext) {
            posts = posts.subList(0, limit);
        }

        return new SliceImpl<>(posts, PageRequest.of(0, limit), hasNext);
    }

    @Transactional
    public void updatePost(Long userId, Long postId, PostUpdateRequest request) {
        Post post = findPostById(postId);

        isPostOwner(post.getUser().getId(), userId);

        User user = findUserById(userId);

        Region region = regionRepository.findById(request.regionId()).orElse(null);

        post.update(request.title(), request.content(), region);

        PostDocument postDocument = PostDocument.from(post, post.getRegion(), user);

        postElasticRepository.save(postDocument);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPostById(postId);

        isPostOwner(post.getUser().getId(), userId);

        postRepository.delete(post);

        postElasticRepository.deleteById(String.valueOf(post.getId()));
    }

    // 헬퍼

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Region findRegionById(Long userId) {
        return regionRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REGION_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private void isPostOwner(Long postUserId, Long userId) {
        if (!Objects.equals(postUserId, userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
