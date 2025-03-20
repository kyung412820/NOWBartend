package com.nowbartend.domain.customer.waiting.controller;

import com.nowbartend.domain.customer.waiting.dto.response.WaitingInfosFindResponse;
import com.nowbartend.domain.customer.waiting.service.WaitingService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/api/waitings")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    @GetMapping
    public ResponseEntity<WaitingInfosFindResponse> findWaitings(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingInfosFindResponse body = waitingService.findWaitingInfos(userDetails.getId(), pageable);

        return ResponseEntity.ok(body);
    }
}
