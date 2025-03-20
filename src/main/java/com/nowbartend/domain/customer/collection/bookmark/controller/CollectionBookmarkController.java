package com.nowbartend.domain.customer.collection.bookmark.controller;

import com.nowbartend.domain.customer.bookmark.dto.response.BookmarksFindResponse;
import com.nowbartend.domain.customer.collection.bookmark.dto.request.CollectionBookmarksCreateRequest;
import com.nowbartend.domain.customer.collection.bookmark.dto.response.CollectionBookmarksFindResponse;
import com.nowbartend.domain.customer.collection.bookmark.service.CollectionBookmarkService;
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
@RequestMapping("/customer/api/collections/{collectionId}")
@RequiredArgsConstructor
public class CollectionBookmarkController {

    private final CollectionBookmarkService collectionBookmarkService;

    @PostMapping("/bookmarks")
    public ResponseEntity<Void> createCollectionBookmarks(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionBookmarksCreateRequest request
    ) {
        collectionBookmarkService
                .createCollectionBookmarks(userDetails.getId(), collectionId, request);
        URI location = UriBuilderUtil.create("/api/customer/collections/{collectionId}", collectionId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<CollectionBookmarksFindResponse> findBookmarks(
            @PathVariable Long collectionId,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CollectionBookmarksFindResponse body
                = collectionBookmarkService.findCollectionBookmarks(userDetails.getId(), collectionId, pageable);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/not-included")
    public ResponseEntity<BookmarksFindResponse> findBookmarksNotInCollection(
            @PathVariable Long collectionId,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookmarksFindResponse body
                = collectionBookmarkService.findBookmarksNotInCollection(userDetails.getId(), collectionId, pageable);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/bookmarks/{collectionBookmarkId}")
    public ResponseEntity<Void> deleteCollectionBookmarks(
            @PathVariable Long collectionId,
            @PathVariable Long collectionBookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        collectionBookmarkService.deleteCollectionBookmark(userDetails.getId(), collectionId, collectionBookmarkId);

        return ResponseEntity.noContent().build();
    }
}
