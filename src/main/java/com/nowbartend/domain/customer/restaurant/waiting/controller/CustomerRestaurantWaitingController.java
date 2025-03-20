package com.nowbartend.domain.customer.restaurant.waiting.controller;

import com.nowbartend.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueCheckUserResponse;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
import com.nowbartend.domain.customer.restaurant.waiting.service.CustomerRestaurantWaitingService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/restaurants/{restaurantId}/waitings")
@RequiredArgsConstructor
public class CustomerRestaurantWaitingController {

    private final CustomerRestaurantWaitingService customerRestaurantWaitingService;

    @PostMapping
    public ResponseEntity<Void> joinWaitingQueue(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody WaitingJoinRequest request
    ) {
        customerRestaurantWaitingService.join(userDetails.getId(), restaurantId, request);

        // redirect uri: 유저 - 나의 예약 목록 조회 API  예정
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<WaitingQueueFindSizeResponse> findWaitingQueueSize(
            @PathVariable Long restaurantId
    ) {
        WaitingQueueFindSizeResponse body = customerRestaurantWaitingService.findWaitingQueueSize(restaurantId);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/check")
    public ResponseEntity<WaitingQueueCheckUserResponse> checkUserInWaitingQueue(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueCheckUserResponse body
                = customerRestaurantWaitingService.checkUserInWaitingQueue(userDetails.getId(), restaurantId);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{waitingId}")
    public ResponseEntity<WaitingQueueFindUserRankResponse> findWaitingQueueUserRank(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueFindUserRankResponse body
                = customerRestaurantWaitingService.findWaitingQueueUserRank(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{waitingId}")
    public ResponseEntity<Void> cancelWaiting(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        customerRestaurantWaitingService.cancelWaitingQueue(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.noContent().build();
    }
}
