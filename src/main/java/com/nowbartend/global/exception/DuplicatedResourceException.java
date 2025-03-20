package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class DuplicatedResourceException extends CustomRuntimeException {
    public DuplicatedResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
