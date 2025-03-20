package com.nowbartend.domain.customer.post.controller;

import com.nowbartend.domain.customer.post.dto.request.PostCreateRequest;
import com.nowbartend.domain.customer.post.dto.request.PostUpdateRequest;
import com.nowbartend.domain.customer.post.dto.response.PostKeywordResponse;
import com.nowbartend.domain.customer.post.dto.response.PostRandomResponse;
import com.nowbartend.domain.customer.post.service.PostElasticService;
import com.nowbartend.domain.customer.post.service.PostService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Random;

@RestController
@RequestMapping("/customer/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostElasticService postElasticService;

    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PostCreateRequest request
    ) {
        Long postId = postService.createPost(userDetails.getId(), request);

        URI location = UriBuilderUtil.create("/customer/api/posts/{postId}", postId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Slice<PostRandomResponse>> findPostsByRandom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long cursorValue, // 마지막 행의 랜덤 값
            @RequestParam(required = false) Integer randomSeed, // 클라이언트가 전달하는 이전 랜덤 시드값
            @RequestParam(defaultValue = "10") int limit
    ) {
        // 커서값이 null 이거나 랜덤시드가 전달되지 않았으면 새로운 랜덤시드 생성
        int seed = (cursorValue == null || randomSeed == null) ? new Random().nextInt() : randomSeed;

        Slice<PostRandomResponse> response = postService.findPostsByRandom(
                userDetails.getId(),
                regionId,
                cursorValue,
                limit,
                seed);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/keyword")
    public ResponseEntity<Slice<PostKeywordResponse>> findPostsByKeyword(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long cursorValue,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Slice<PostKeywordResponse> response = postService.findPostsByKeyword(
                userId,
                regionId,
                cursorValue,
                keyword,
                limit
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/keyword/elastic")
    public ResponseEntity<Slice<PostKeywordResponse>> findPostsByKeywordWithElastic(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long cursorValue,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Slice<PostKeywordResponse> response = postElasticService.findPostsByKeywordWithElastic(
                regionId,
                cursorValue,
                keyword,
                limit);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateRequest request
    ) {
        postService.updatePost(userDetails.getId(), postId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        postService.deletePost(postId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
