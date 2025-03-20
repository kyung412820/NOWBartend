package com.nowbartend.domain.common.user.controller;

import com.nowbartend.domain.common.user.dto.request.UserUpdateInfoRequest;
import com.nowbartend.domain.common.user.dto.request.UserUpdatePasswordRequest;
import com.nowbartend.domain.common.user.dto.request.UserUpdateProfileRequest;
import com.nowbartend.domain.common.user.dto.response.UserMyInfoResponse;
import com.nowbartend.domain.common.user.dto.response.UserMyPageResponse;
import com.nowbartend.domain.common.user.dto.response.UserPageResponse;
import com.nowbartend.domain.common.user.dto.response.UserProfileResponse;
import com.nowbartend.domain.common.user.service.UserService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMyPageResponse> findMyPage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findMyPage(userDetails.getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPageResponse> findUserProfile(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findUserProfile(userId));
    }

    @GetMapping("/me/info")
    public ResponseEntity<UserMyInfoResponse> findMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findMyInfo(userDetails.getId()));
    }

    @PatchMapping("/me/info/passwd")
    public ResponseEntity<Void> updateMyPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdatePasswordRequest request
    ) {
        userService.updateMyPassword(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> findMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findMyProfile(userDetails.getId()));
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdateProfileRequest request
    ) {
        userService.updateMyProfile(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/info")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdateInfoRequest request
    ) {
        userService.updateMyInfo(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }
}
