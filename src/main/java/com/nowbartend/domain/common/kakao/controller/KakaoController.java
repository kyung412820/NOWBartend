package com.nowbartend.domain.common.kakao.controller;


import com.nowbartend.domain.common.kakao.service.KakaoService;
import com.nowbartend.domain.common.auth.dto.response.AuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<AuthLoginResponse> getToken(@RequestParam("access_token") String accessToken) {

        AuthLoginResponse response = kakaoService.kakaoLogin(accessToken);

        return ResponseEntity.ok(response);
    }
}
