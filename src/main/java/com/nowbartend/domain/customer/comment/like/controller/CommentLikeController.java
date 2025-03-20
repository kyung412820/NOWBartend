package com.nowbartend.domain.customer.comment.like.controller;

import com.nowbartend.domain.customer.comment.like.service.CommentLikeService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/comments/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PutMapping
    public ResponseEntity<Void> likeAndUnlikeComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long commentId
    ) {
        commentLikeService.likeAndUnlikeComment(commentId, userDetails.getId());

        return ResponseEntity.ok().build();
    }
}
