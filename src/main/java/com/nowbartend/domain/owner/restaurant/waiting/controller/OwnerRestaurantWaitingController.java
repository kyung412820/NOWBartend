package com.nowbartend.domain.owner.restaurant.waiting.controller;

import com.nowbartend.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteUserRequest;
import com.nowbartend.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.nowbartend.domain.owner.restaurant.waiting.service.OwnerRestaurantWaitingService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/api/restaurants/{restaurantId}/waitings")
@RequiredArgsConstructor
public class OwnerRestaurantWaitingController {

    private final OwnerRestaurantWaitingService ownerRestaurantWaitingService;

    @GetMapping("/queue")
    public ResponseEntity<WaitingQueueFindUsersResponse> findWaitingQueue(
            @PathVariable Long restaurantId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueFindUsersResponse body = ownerRestaurantWaitingService
                .findWaitingUsers(userDetails.getId(), restaurantId, page, size);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{waitingId}/call-waiting")
    public ResponseEntity<Void> callWaitingUser(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ownerRestaurantWaitingService.callRequestWaitingUser(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{waitingId}/complete-waiting")
    public ResponseEntity<Void> completeWaitingUser(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ownerRestaurantWaitingService.completeWaitingUser(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWaitingUser(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid WaitingQueueDeleteUserRequest request
    ) {
        ownerRestaurantWaitingService
                .deleteUserFromWaitingQueue(userDetails.getId(), restaurantId, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateWaitingStatus(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ownerRestaurantWaitingService.updateWaitingStatus(userDetails.getId(), restaurantId);

        return ResponseEntity.ok().build();
    }
}
