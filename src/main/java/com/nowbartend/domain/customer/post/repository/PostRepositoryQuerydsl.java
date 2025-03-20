package com.nowbartend.domain.customer.post.repository;

import com.nowbartend.domain.customer.post.dto.response.PostCounts;
import com.nowbartend.domain.customer.post.dto.response.PostKeywordResponse;
import com.nowbartend.domain.customer.post.dto.response.PostRandomResponse;
import com.nowbartend.domain.customer.post.entity.Post;

import java.util.List;

public interface PostRepositoryQuerydsl {

    List<PostRandomResponse> findPostsByRandom(Long userId, Long regionId, Long cursorValue, int limit, int randomSeed);

    List<PostKeywordResponse> findPostsByKeyword(Long userId, Long regionId, Long cursorValue, String keyword, int limit);

    List<PostCounts> findAllByIdWithCommentsAndLikes(List<Long> postIds);
}
