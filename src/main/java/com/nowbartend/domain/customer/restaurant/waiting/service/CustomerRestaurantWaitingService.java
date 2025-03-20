package com.nowbartend.domain.customer.restaurant.waiting.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.restaurant.repository.CustomerRestaurantRepository;
import com.nowbartend.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueCheckUserResponse;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.nowbartend.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
import com.nowbartend.domain.customer.restaurant.waiting.repository.CustomerRestaurantWaitingRedisRepository;
import com.nowbartend.domain.customer.restaurant.waiting.repository.CustomerRestaurantWaitingRepository;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.entity.RestaurantWaitingStatus;
import com.nowbartend.domain.owner.restaurant.waiting.entity.Waiting;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingStatus;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingType;
import com.nowbartend.global.exception.ConflictException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRestaurantWaitingService {

    private final AuthService authService;
    private final CustomerRestaurantWaitingJoinService customerRestaurantWaitingJoinService;

    private final UserRepository userRepository;
    private final CustomerRestaurantRepository customerRestaurantRepository;
    private final CustomerRestaurantWaitingRepository customerRestaurantWaitingRepository;
    private final CustomerRestaurantWaitingRedisRepository customerRestaurantWaitingRedisRepository;

    @Transactional
    public void join(Long userId, Long restaurantId, WaitingJoinRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        String waitingKey = WaitingType.IN.getWaitingKey(restaurant.getId());

        boolean isExists = customerRestaurantWaitingRedisRepository.zFindUserRank(waitingKey, user.getId())
                .isPresent();

        if (isExists) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }

        int dailySequence = customerRestaurantWaitingJoinService.joinWaitingQueue(user.getId(), restaurant.getId(), request);

        customerRestaurantWaitingRedisRepository.join(waitingKey, user.getId(), dailySequence);
    }

    @Transactional
    public void cancelWaitingQueue(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        Waiting waiting = findWaitingById(waitingId);

        User user = findUserById(userId);
        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        if (!waiting.getStatus().equals(WaitingStatus.REQUESTED)) {
            throw new ConflictException(ErrorCode.INVALID_WAITING_CANCEL_STATUS);
        }

        String key = waiting.getType().getWaitingKey(waiting.getRestaurant().getId());

        customerRestaurantWaitingRedisRepository.zFindUserRank(key, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        waiting.updateStatus(WaitingStatus.CANCELED);

        customerRestaurantWaitingRedisRepository.zRemove(key, user.getId());

        // 유저에게 대기열 취소에 성공함을 알리는 알림 전송 추가
    }

    public WaitingQueueFindSizeResponse findWaitingQueueSize(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        String waitingKey = WaitingType.IN.getWaitingKey(restaurant.getId());
        Long waitingQueueSize = customerRestaurantWaitingRedisRepository.zCard(waitingKey);

        return WaitingQueueFindSizeResponse.from(waitingQueueSize);
    }

    public WaitingQueueCheckUserResponse checkUserInWaitingQueue(Long userId, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);
        String key = WaitingType.IN.getWaitingKey(restaurant.getId());

        boolean status = customerRestaurantWaitingRedisRepository.zFindUserRank(key, user.getId())
                .isPresent();

        return WaitingQueueCheckUserResponse.from(status);
    }

    public WaitingQueueFindUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);
        Waiting waiting = findWaitingById(waitingId);

        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        String key = waiting.getType().getWaitingKey(restaurant.getId());

        Long rank = customerRestaurantWaitingRedisRepository.zFindUserRank(key, user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        return WaitingQueueFindUserRankResponse.from(rank);
    }

    private void isPossibleWaiting(RestaurantWaitingStatus waitingStatus) {
        if (waitingStatus.equals(RestaurantWaitingStatus.CLOSE)) {
            throw new ConflictException(ErrorCode.RESTAURANT_WAITING_IS_CLOSED);
        }
    }

    private Waiting findWaitingById(Long waitingId) {
        return customerRestaurantWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return customerRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
