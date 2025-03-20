package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
}
