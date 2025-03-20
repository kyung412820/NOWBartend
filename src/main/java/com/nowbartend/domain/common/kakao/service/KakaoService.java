package com.nowbartend.domain.common.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowbartend.domain.common.kakao.dto.response.KakaoAccount;
import com.nowbartend.domain.common.kakao.dto.response.KakaoInfo;
import com.nowbartend.domain.common.kakao.dto.response.OAuthToken;
import com.nowbartend.domain.common.auth.dto.response.AuthLoginResponse;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.entity.UserRole;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.error.ErrorCode;
import com.nowbartend.global.security.JwtTokenProvider;
import com.nowbartend.global.security.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 카카오 API 호출을 위한 HTTP 클라이언트

    @Value("${kakao.auth.client}")
    private String clientId;

    @Value("${kakao.auth.redirect}")
    private String redirectUri;

    private final long DURATION = Duration.ofDays(3).toMillis();

    public AuthLoginResponse kakaoLogin(String accessToken) {

        // 코드로 카카오에서 액세스 토큰 받아오기
//        String accessToken = getAccessTokenFromKakao(code);

        // 액세스 토큰으로 카카오 사용자 정보 받아오기
        KakaoAccount kakaoAccount = getKakaoUserInfo(accessToken);

        // 전화번호 변환: "+82 10-xxxx-xxxx" -> "010xxxxxxxx"
        String kakaoPhoneNumber = kakaoAccount.getPhoneNumber();

        log.info("Kakao로부터 받은 전화번호: {}", kakaoPhoneNumber);

        String phoneNumber = kakaoPhoneNumber.replace("+82", "0").replaceAll("[\\s-]", "");

        log.info("변환 된 전화번호 : {}", phoneNumber);

        // 기존 사용자 확인 후 회원가입 후 로그인 처리
        User user = userRepository.findByEmail(kakaoAccount.getEmail())
                .orElseGet(() -> {
                    // 신규 회원 가입
                    User newUser = User.builder()
                            .nickname(kakaoAccount.getProfile().getNickname())
                            .email(kakaoAccount.getEmail())
                            .name(kakaoAccount.getName())
                            .phoneNumber(phoneNumber)
                            .role(UserRole.CUSTOMER)
                            .build();
                    return userRepository.save(newUser);
                });

        // JWT 토큰 발행
        String userId = String.valueOf(user.getId());
        String jwtAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken();

        final long accessTokenExpiryTime = jwtTokenProvider.getAccessTokenExpiryTime(jwtAccessToken);

        // refresh token 저장 (redis 등)
        redisRepository.setDataWithExpire(userId, jwtRefreshToken, DURATION);

        return AuthLoginResponse.of(jwtAccessToken, jwtRefreshToken, accessTokenExpiryTime);
    }

    // 액세스 토큰 교환 메소드
    private String getAccessTokenFromKakao(String code) {


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId); // 카카오 앱 키 입력
        params.add("redirect_uri", redirectUri); // 설정한 Redirect URI
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오 토큰 엔드포인트 호출
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
            log.info("oAuthToken : " + oAuthToken.getAccessToken());

        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(ErrorCode.KAKAO_FAILED_TOKEN_PARSING_EXCEPTION);
        }
        return oAuthToken.getAccessToken();
    }

    // 사용자 정보 요청 메소드
    private KakaoAccount getKakaoUserInfo(String accessToken) {

        // 요청 헤더에 Bearer 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 헤더로 요청값 생성
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // 카카오 사용자 정보 엔드포인트 호출
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",    // 사용자 정보 들어있는 URI
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );

        KakaoInfo kakaoProfile = null;

        // 응답값을 KakaoInfo 로 파싱
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoInfo.class);

        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(ErrorCode.KAKAO_FAILED_PROFILE_PARSING_EXCEPTION);
        }
        return kakaoProfile.getKakaoAccount();
    }
}
