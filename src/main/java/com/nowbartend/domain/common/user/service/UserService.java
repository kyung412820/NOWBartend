package com.nowbartend.domain.common.user.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.user.dto.request.UserUpdateInfoRequest;
import com.nowbartend.domain.common.user.dto.request.UserUpdatePasswordRequest;
import com.nowbartend.domain.common.user.dto.request.UserUpdateProfileRequest;
import com.nowbartend.domain.common.user.dto.response.UserMyInfoResponse;
import com.nowbartend.domain.common.user.dto.response.UserMyPageResponse;
import com.nowbartend.domain.common.user.dto.response.UserPageResponse;
import com.nowbartend.domain.common.user.dto.response.UserProfileResponse;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.follow.repository.FollowRepository;
import com.nowbartend.domain.customer.post.region.entity.Region;
import com.nowbartend.domain.customer.post.region.repository.RegionRepository;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthService authService;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Transactional(readOnly = true)
    public UserMyPageResponse findMyPage(Long userId) {
        User user = findUserById(userId);

        long followerCount = followRepository.countFollower(userId);
        long followingCount = followRepository.countFollowing(userId);

        return UserMyPageResponse.of(user, followerCount, followingCount);
    }

    @Transactional(readOnly = true)
    public UserPageResponse findUserProfile(Long userId) {
        User user = findUserById(userId);

        long followerCount = followRepository.countFollower(userId);
        long followingCount = followRepository.countFollowing(userId);

        return UserPageResponse.of(user, followerCount, followingCount);
    }

    @Transactional(readOnly = true)
    public UserMyInfoResponse findMyInfo(Long userId) {
        User user = findUserById(userId);

        return UserMyInfoResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse findMyProfile(Long userId) {
        User user = findUserById(userId);

        return UserProfileResponse.from(user);
    }

    @Transactional
    public void updateMyProfile(Long userId, UserUpdateProfileRequest request) {
        User user = findUserById(userId);

        // 요청에서 isChangeRegion == true 일 때만 지역을 변경해준다
        if (request.isChangeRegion()) {
            Region region = request.regionId() != null ?
                    regionRepository.findById(request.regionId())
                            .orElseThrow(() -> new NotFoundException(ErrorCode.REGION_NOT_FOUND)) : null;

            user.updateRegion(region);
        }

        user.updateProfile(request.nickname(), request.introduce());
    }

    @Transactional
    public void updateMyInfo(Long userId, UserUpdateInfoRequest request) {
        User user = findUserById(userId);

        user.updateInfo(request.name(), request.phoneNumber());
    }

    @Transactional
    public void updateMyPassword(Long userId, UserUpdatePasswordRequest request) {
        User user = findUserById(userId);

        user.resetPassword(authService.encodePassword(request.password()));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
