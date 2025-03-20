package com.nowbartend.domain.customer.bookmark.repository;

import com.nowbartend.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.nowbartend.domain.customer.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkQRepository {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    List<Bookmark> findAllByBookmarkIds(List<Long> bookmarkIds);

    Page<BookmarkDetails> findBookmarkDetailsPaginationByUserId(Long userId, Pageable pageable);

    Page<BookmarkDetails> findBookmarkDetailsByUserIdAndNotInCollection(Long userId, Long collectionId, Pageable pageable);
}
