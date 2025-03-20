package com.nowbartend.domain.customer.follow.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.service.UserService;
import com.nowbartend.domain.customer.follow.dto.response.FollowUserResponse;
import com.nowbartend.domain.customer.follow.entity.Follow;
import com.nowbartend.domain.customer.follow.repository.FollowRepository;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final UserService userService;
    private final FollowRepository followRepository;

    @Transactional
    @CacheEvict(value = {"followers", "followings"}, allEntries = true)
    public Long followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new CustomRuntimeException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_FOLLOWING);
        }

        User follower = userService.findUserById(followerId);
        User following = userService.findUserById(followingId);

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        return follow.getId();
    }

    @Transactional
    @CacheEvict(value = {"followers", "followings"}, allEntries = true)
    public void unfollowUser(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    @Cacheable(value = "followers", key = "'followers_' + #userId + '_' + (#cursor ?: 0) + '_' + #limit")
    public List<FollowUserResponse> findFollowers(Long userId, Long cursor, int limit) {
        return followRepository.findFollowersByFollowingId(userId, cursor, limit)
                .stream()
                .map(FollowUserResponse::from)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "followers", key = "'followers_' + #userId + '_' + (#cursor ?: 0) + '_' + #limit")
    public List<FollowUserResponse> findFollowings(Long userId, Long cursor, int limit) {
        return followRepository.findFollowingsByFollowerId(userId, cursor, limit)
                .stream()
                .map(FollowUserResponse::from)
                .collect(Collectors.toList());
    }
}