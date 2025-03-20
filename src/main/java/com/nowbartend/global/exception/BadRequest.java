package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class BadRequest extends CustomRuntimeException {
    public BadRequest(ErrorCode errorCode) {
        super(errorCode);
    }
}
