package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class ConflictException extends CustomRuntimeException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
