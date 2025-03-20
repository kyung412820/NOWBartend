package com.nowbartend.domain.customer.collection.controller;

import com.nowbartend.domain.customer.collection.dto.request.CollectionCreateRequest;
import com.nowbartend.domain.customer.collection.dto.request.CollectionUpdateRequest;
import com.nowbartend.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.nowbartend.domain.customer.collection.dto.response.CollectionInfosResponse;
import com.nowbartend.domain.customer.collection.service.CollectionService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/customer/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<Void> createCollection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionCreateRequest request
    ) {
        Long collectionId = collectionService.createCollection(userDetails.getId(), request);
        URI location = UriBuilderUtil.create("/api/collections/{collectionId}", collectionId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailResponse> findCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CollectionDetailResponse body = collectionService.findCollection(userDetails.getId(), collectionId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<CollectionInfosResponse> findCollections(
            @RequestParam Long userId,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CollectionInfosResponse body = collectionService.findCollections(userDetails.getId(), userId, pageable);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<Void> updateCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionUpdateRequest request
    ) {
        collectionService.updateCollection(userDetails.getId(), collectionId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        collectionService.deleteCollection(userDetails.getId(), collectionId);

        return ResponseEntity.noContent().build();
    }
}
