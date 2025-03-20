package com.nowbartend.domain.customer.post.like.controller;

import com.nowbartend.domain.customer.post.like.service.PostLikeService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/posts/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PutMapping
    public ResponseEntity<Void> likeAndUnlikePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long postId

    ) {
        postLikeService.likeAndUnlikePost(postId, userDetails.getId());

        return ResponseEntity.ok().build();
    }
}
