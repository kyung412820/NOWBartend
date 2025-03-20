package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class ForbiddenException extends CustomRuntimeException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
