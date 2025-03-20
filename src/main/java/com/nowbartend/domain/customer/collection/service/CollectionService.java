package com.nowbartend.domain.customer.collection.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.collection.dto.request.CollectionCreateRequest;
import com.nowbartend.domain.customer.collection.dto.request.CollectionUpdateRequest;
import com.nowbartend.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.nowbartend.domain.customer.collection.dto.response.CollectionInfoResponse;
import com.nowbartend.domain.customer.collection.dto.response.CollectionInfosResponse;
import com.nowbartend.domain.customer.collection.entity.Collection;
import com.nowbartend.domain.customer.collection.repository.CollectionRepository;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public Long createCollection(Long userId, CollectionCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Collection collection = Collection.builder()
                .user(user)
                .name(request.name())
                .description(request.description())
                .isPublic(request.isPublic())
                .build();

        Collection savedCollection = collectionRepository.save(collection);

        return savedCollection.getId();
    }

    public CollectionDetailResponse findCollection(Long userId, Long collectionId) {
        CollectionDetailResponse collectionDetailResponse = collectionRepository.findCollectionDetail(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        if (!collectionDetailResponse.isPublic()) {
            authService.checkUserAuthority(collectionDetailResponse.userId(), userId);
        }

        return collectionDetailResponse;
    }

    public CollectionInfosResponse findCollections(Long userId, Long collectionOwnerId, Pageable pageable) {
        Page<CollectionInfoResponse> collectionInfos = null;

        if (userId.equals(collectionOwnerId)) {
            collectionInfos = collectionRepository.findAllByCollectionOwnerId(collectionOwnerId, pageable);
        }

        if (!userId.equals(collectionOwnerId)) {
            collectionInfos = collectionRepository.findAllByPublicCollections(collectionOwnerId, pageable);
        }

        return CollectionInfosResponse.from(collectionInfos);
    }

    @Transactional
    public void updateCollection(Long userId, Long collectionId, CollectionUpdateRequest request) {
        Collection collection = findById(collectionId);

        authService.checkUserAuthority(collection.getUser().getId(), userId);

        collection.updateCollectionInfo(request);
    }

    @Transactional
    public void deleteCollection(Long userId, Long collectionId) {
        Collection collection = findById(collectionId);

        authService.checkUserAuthority(collection.getUser().getId(), userId);

        collectionRepository.delete(collection);
    }

    private Collection findById(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));
    }
}
