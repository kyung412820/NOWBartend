package com.nowbartend.domain.customer.follow.controller;

import static com.nowbartend.domain.customer.follow.dto.FollowValidationMessage.*;

import com.nowbartend.domain.customer.follow.dto.response.FollowUserResponse;
import com.nowbartend.domain.customer.follow.service.FollowService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @Positive(message = FOLLOWING_ID_POSITIVE) Long followingId
    ) {
        Long followId = followService.followUser(userDetails.getId(), followingId);

        URI location = UriBuilderUtil.create("/customer/api/follows", followId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @Positive(message = FOLLOWING_ID_POSITIVE) Long followingId
    ) {
        followService.unfollowUser(userDetails.getId(), followingId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowUserResponse>> findFollowers(
            @PathVariable @Positive(message = USER_ID_POSITIVE) Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Positive(message = LIMIT_POSITIVE) int limit
    ) {
        return ResponseEntity.ok(followService.findFollowers(userId, cursor, limit));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<FollowUserResponse>> findFollowings(
            @PathVariable @Positive(message = USER_ID_POSITIVE) Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Positive(message = LIMIT_POSITIVE) int limit
    ) {
        return ResponseEntity.ok(followService.findFollowings(userId, cursor, limit));
    }
}
