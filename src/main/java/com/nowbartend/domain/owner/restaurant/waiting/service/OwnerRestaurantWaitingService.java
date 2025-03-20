package com.nowbartend.domain.owner.restaurant.waiting.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.service.OwnerRestaurantService;
import com.nowbartend.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteUserRequest;
import com.nowbartend.domain.owner.restaurant.waiting.dto.response.WaitingDetails;
import com.nowbartend.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.nowbartend.domain.owner.restaurant.waiting.entity.Waiting;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingStatus;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingType;
import com.nowbartend.domain.owner.restaurant.waiting.repository.OwnerRestaurantWaitingRedisRepository;
import com.nowbartend.domain.owner.restaurant.waiting.repository.OwnerRestaurantWaitingRepository;
import com.nowbartend.global.exception.ConflictException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerRestaurantWaitingService {

    private final AuthService authService;
    private final OwnerRestaurantService ownerRestaurantService;

    private final OwnerRestaurantWaitingRepository ownerRestaurantWaitingRepository;
    private final OwnerRestaurantWaitingRedisRepository ownerRestaurantWaitingRedisRepository;

    public WaitingQueueFindUsersResponse findWaitingUsers(
            Long ownerId,
            Long restaurantId,
            int page,
            int size
    ) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        String key = WaitingType.IN.getWaitingKey(restaurant.getId());

        Long totalElements = ownerRestaurantWaitingRedisRepository.findQueueSize(key);

        int totalPages = (int) Math.ceil((double) totalElements / size);

        if (totalPages < 0) {
            totalPages = 0;
        }

        List<WaitingDetails> waitingUserDetails = Collections.emptyList();

        if (totalElements == 0) {
            return WaitingQueueFindUsersResponse.from(waitingUserDetails, page, totalPages, totalElements);
        }

        List<Long> userIds = ownerRestaurantWaitingRedisRepository.findWaitingUsers(key, page, size);

        if (!userIds.isEmpty()) {
            waitingUserDetails = ownerRestaurantWaitingRepository.findWaitingDetails(userIds);
        }

        return WaitingQueueFindUsersResponse.from(waitingUserDetails, page, totalPages, totalElements);
    }

    @Transactional
    public void callRequestWaitingUser(Long ownerId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        Waiting waiting = findWaitingById(waitingId);

        WaitingType waitingType = waiting.getType();
        String key = waitingType.getWaitingKey(restaurant.getId());

        if (!waiting.getStatus().equals(WaitingStatus.REQUESTED)) {
            throw new ConflictException(ErrorCode.WAITING_STATUS_CHANGE_NOT_ALLOWED);
        }

        Long firstRankedUserId = ownerRestaurantWaitingRedisRepository.findFirstRankedUser(key)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));

        if (!waiting.getUser().getId().equals(firstRankedUserId)) {
            throw new ConflictException(ErrorCode.WAITING_STATUS_CHANGE_NOT_ALLOWED);
        }

        waiting.updateStatus(WaitingStatus.NOTIFIED);

        // 입장 요청 알림
    }

    @Transactional
    public void completeWaitingUser(Long ownerId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        Waiting waiting = findWaitingById(waitingId);
        WaitingType waitingType = waiting.getType();
        String key = waitingType.getWaitingKey(restaurant.getId());

        if (!waiting.getStatus().equals(WaitingStatus.NOTIFIED)) {
            throw new ConflictException(ErrorCode.WAITING_STATUS_CHANGE_NOT_ALLOWED);
        }

        Long firstRankedUserId = ownerRestaurantWaitingRedisRepository.findFirstRankedUser(key)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));

        if (!waiting.getUser().getId().equals(firstRankedUserId)) {
            throw new ConflictException(ErrorCode.WAITING_STATUS_CHANGE_NOT_ALLOWED);
        }

        waiting.updateStatus(WaitingStatus.COMPLETED);

        Long deleteCount = ownerRestaurantWaitingRedisRepository.deleteWaitingUser(key, waiting.getUser().getId());

        if (deleteCount == 0) {
            throw new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND);
        }

        // 입장 완료 알림
    }

    @Transactional
    public void deleteUserFromWaitingQueue(Long ownerId, Long restaurantId, WaitingQueueDeleteUserRequest request) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        Waiting waiting = findWaitingById(request.waitingId());

        if (!waiting.getStatus().equals(WaitingStatus.REQUESTED) && !waiting.getStatus().equals(WaitingStatus.NOTIFIED)) {
            throw new ConflictException(ErrorCode.INVALID_WAITING_CANCEL_STATUS);
        }

        WaitingType waitingType = waiting.getType();
        String key = waitingType.getWaitingKey(restaurant.getId());

        waiting.updateStatus(WaitingStatus.CANCELED);

        Long deleteCount = ownerRestaurantWaitingRedisRepository.deleteWaitingUser(key, waiting.getUser().getId());

        if (deleteCount == 0) {
            throw new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND);
        }

        // 삭제된 유저에게 취소 됨을 알리는 알림 전송 추가
    }

    @Transactional
    public void updateWaitingStatus(Long ownerId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        restaurant.switchWaitingStatus();
    }

    private Waiting findWaitingById(Long waitingId) {
        return ownerRestaurantWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));
    }
}
