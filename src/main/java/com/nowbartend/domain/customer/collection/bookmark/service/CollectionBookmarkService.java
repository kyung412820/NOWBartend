package com.nowbartend.domain.customer.collection.bookmark.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.nowbartend.domain.customer.bookmark.dto.response.BookmarksFindResponse;
import com.nowbartend.domain.customer.bookmark.entity.Bookmark;
import com.nowbartend.domain.customer.bookmark.repository.BookmarkRepository;
import com.nowbartend.domain.customer.collection.bookmark.dto.request.CollectionBookmarkCreateRequest;
import com.nowbartend.domain.customer.collection.bookmark.dto.request.CollectionBookmarksCreateRequest;
import com.nowbartend.domain.customer.collection.bookmark.dto.response.CollectionBookmarkDetails;
import com.nowbartend.domain.customer.collection.bookmark.dto.response.CollectionBookmarksFindResponse;
import com.nowbartend.domain.customer.collection.bookmark.entity.CollectionBookmark;
import com.nowbartend.domain.customer.collection.bookmark.repository.CollectionBookmarkRepository;
import com.nowbartend.domain.customer.collection.entity.Collection;
import com.nowbartend.domain.customer.collection.repository.CollectionRepository;
import com.nowbartend.global.exception.BadRequest;
import com.nowbartend.global.exception.ConflictException;
import com.nowbartend.global.exception.ForbiddenException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionBookmarkService {

    private final CollectionBookmarkRepository collectionBookmarkRepository;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AuthService authService;

    @Transactional
    public void createCollectionBookmarks(
            Long userId,
            Long collectionId,
            CollectionBookmarksCreateRequest request
    ) {
        List<Long> bookmarkIds = request.bookmarks().stream()
                .map(CollectionBookmarkCreateRequest::bookmarkId)
                .toList();

        if (collectionBookmarkRepository.existsByCollectionIdAndBookmarkIds(collectionId, bookmarkIds)) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_BOOKMARK_IN_COLLECTION);
        }

        Collection collection = findCollectionById(collectionId);

        authService.checkUserAuthority(collection.getUser().getId(), userId);

        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkIds(bookmarkIds);

        // 요청한 id 값들로 조회한 bookmark 레코드들이 가져와졌는지 검사
        // 이후 CollectionBookmark 객체 리스트로 mapping
        List<CollectionBookmark> collectionBookmarks = request.bookmarks().stream()
                .map(collectionBookmark -> {
                    Bookmark findBookmark = bookmarks.stream()
                            .filter(bookmark -> bookmark.getId().equals(collectionBookmark.bookmarkId()))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

                    authService.checkUserAuthority(findBookmark.getUser().getId(), userId);

                    return CollectionBookmark.builder()
                            .collection(collection)
                            .bookmark(findBookmark)
                            .build();
                })
                .toList();

        collectionBookmarkRepository.saveAll(collectionBookmarks);
    }

    public CollectionBookmarksFindResponse findCollectionBookmarks(Long userId, Long collectionId, Pageable pageable) {
        Collection collection = findCollectionById(collectionId);

        if (!collection.isPublic()) {
            authService.checkUserAuthority(collection.getUser().getId(), userId);
        }

        Page<CollectionBookmarkDetails> bookmarkDetails
                = collectionBookmarkRepository.findBookmarkDetailsByCollectionId(collectionId, pageable);

        return CollectionBookmarksFindResponse.from(bookmarkDetails);
    }

    public BookmarksFindResponse findBookmarksNotInCollection(Long userId, Long collectionId, Pageable pageable) {
        Page<BookmarkDetails> unBookmarkedBookmarks = bookmarkRepository.findBookmarkDetailsByUserIdAndNotInCollection(userId, collectionId, pageable);

        return BookmarksFindResponse.from(unBookmarkedBookmarks);
    }

    @Transactional
    public void deleteCollectionBookmark(Long userId, Long collectionId, Long collectionBookmarkId) {
        CollectionBookmark collectionBookmark = findById(collectionBookmarkId);

        if (!collectionBookmark.getCollection().getId().equals(collectionId)) {
            throw new BadRequest(ErrorCode.INVALID_ACCESS_BOOKMARK_IN_COLLECTION);
        }

        authService.checkUserAuthority(collectionBookmark.getBookmark().getUser().getId(), userId);

        collectionBookmarkRepository.delete(collectionBookmark);
    }

    private CollectionBookmark findById(Long collectionBookmarkId) {
        return collectionBookmarkRepository.findByCollectionBookmarkId(collectionBookmarkId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));
    }

    private Collection findCollectionById(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));
    }
}
