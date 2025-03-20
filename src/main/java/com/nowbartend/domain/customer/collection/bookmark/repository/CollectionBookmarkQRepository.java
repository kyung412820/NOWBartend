package com.nowbartend.domain.customer.collection.bookmark.repository;

import com.nowbartend.domain.customer.collection.bookmark.dto.response.CollectionBookmarkDetails;
import com.nowbartend.domain.customer.collection.bookmark.entity.CollectionBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CollectionBookmarkQRepository {

    Optional<CollectionBookmark> findByCollectionBookmarkId(Long collectionBookmarkId);

    boolean existsByCollectionIdAndBookmarkIds(Long collectionId, List<Long> bookmarkIds);

    Page<CollectionBookmarkDetails> findBookmarkDetailsByCollectionId(Long collectionId, Pageable pageable);
}
