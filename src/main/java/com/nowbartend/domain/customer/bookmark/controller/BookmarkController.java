package com.nowbartend.domain.customer.bookmark.controller;

import com.nowbartend.domain.customer.bookmark.dto.request.BookmarkCreateRequest;
import com.nowbartend.domain.customer.bookmark.dto.request.BookmarkUpdateRequest;
import com.nowbartend.domain.customer.bookmark.dto.response.BookmarksFindResponse;
import com.nowbartend.domain.customer.bookmark.service.BookmarkService;
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
@RequestMapping("/customer/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BookmarkCreateRequest request
    ) {
        bookmarkService.createBookmark(userDetails.getId(), request.restaurantId());

        String path = "/customer/api/restaurants/{restaurantsId}";
        URI location = UriBuilderUtil.create(path, request.restaurantId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<BookmarksFindResponse> findBookmarks(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookmarksFindResponse body = bookmarkService.findBookmarks(userDetails.getId(), pageable);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{bookmarkId}")
    public ResponseEntity<Void> updateBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BookmarkUpdateRequest request
    ) {
        bookmarkService.updateBookmarkMemo(userDetails.getId(), bookmarkId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        bookmarkService.deleteBookmark(userDetails.getId(), bookmarkId);

        return ResponseEntity.noContent().build();
    }
}