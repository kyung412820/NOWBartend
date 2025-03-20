package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
