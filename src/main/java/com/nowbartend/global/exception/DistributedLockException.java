package com.nowbartend.global.exception;

import com.nowbartend.global.exception.error.ErrorCode;

public class DistributedLockException extends CustomRuntimeException {
    public DistributedLockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
