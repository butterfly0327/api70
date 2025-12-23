package com.yumyumcoach.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 기본 메시지
        this.errorCode = errorCode;
    }

    // message 오버라이드
    public BusinessException(ErrorCode errorCode, String messageOverride) {
        super(messageOverride);
        this.errorCode = errorCode;
    }
}

