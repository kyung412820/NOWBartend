package com.nowbartend.domain.common.auth.service;

import com.nowbartend.domain.common.auth.dto.request.AccessTokenReissueReq;
import com.nowbartend.domain.common.auth.dto.request.AuthLoginRequest;
import com.nowbartend.domain.common.auth.dto.request.AuthSignupRequest;
import com.nowbartend.domain.common.auth.dto.response.AuthLoginResponse;
import com.nowbartend.domain.common.auth.dto.response.AuthSignupResponse;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.global.exception.error.ErrorCode;
import com.nowbartend.global.security.JwtTokenProvider;
import com.nowbartend.global.security.model.TokenType;
import com.nowbartend.global.security.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final long DURATION = Duration.ofDays(3).toMillis();

    @Transactional
    public AuthSignupResponse signup(AuthSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicatedResourceException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = request.toEntity(encodePassword(request.password()));

        User savedUser = userRepository.save(user);

        return AuthSignupResponse.from(savedUser);
    }

    @Transactional
    public AuthLoginResponse signin(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidException(ErrorCode.INVALID_PASSWORD);
        }

        String userId = String.valueOf(user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        final long accessTokenExpiryTime = jwtTokenProvider.getAccessTokenExpiryTime(accessToken);

        redisRepository.setDataWithExpire(userId, refreshToken, DURATION);

        return AuthLoginResponse.of(accessToken, refreshToken, accessTokenExpiryTime);
    }

    @Transactional
    public AuthLoginResponse recreateAccessAndRefreshToken(AccessTokenReissueReq accessTokenReissueReq) {
        String refreshToken = accessTokenReissueReq.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken, TokenType.REFRESH)) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String userId = redisRepository.getData(refreshToken);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();
        final long accessTokenExpiryTime = jwtTokenProvider.getAccessTokenExpiryTime(newAccessToken);

        redisRepository.setDataWithExpire(userId, newRefreshToken, DURATION);

        return AuthLoginResponse.of(newAccessToken, newRefreshToken, accessTokenExpiryTime);
    }

    public void checkUserAuthority(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
