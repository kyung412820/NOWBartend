package com.nowbartend.domain.common.auth.controller;

import com.nowbartend.domain.common.auth.dto.request.AccessTokenReissueReq;
import com.nowbartend.domain.common.auth.dto.request.AuthLoginRequest;
import com.nowbartend.domain.common.auth.dto.request.AuthSignupRequest;
import com.nowbartend.domain.common.auth.dto.response.AuthLoginResponse;
import com.nowbartend.domain.common.auth.dto.response.AuthSignupResponse;
import com.nowbartend.domain.common.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthSignupResponse> signup(
            @RequestBody @Valid AuthSignupRequest request
    ) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthLoginResponse> signin(
            @RequestBody @Valid AuthLoginRequest request
    ) {
        return ResponseEntity.ok(authService.signin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> reissueAccessToken(
            @RequestBody AccessTokenReissueReq accessTokenReissueReq
    ) {
        AuthLoginResponse AuthLoginResponse = authService.recreateAccessAndRefreshToken(
                accessTokenReissueReq);
        return ResponseEntity.ok(AuthLoginResponse);
    }
}
