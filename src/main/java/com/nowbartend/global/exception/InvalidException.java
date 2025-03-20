package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
