package com.yumyumcoach.global.exception;

public record ErrorResponse(int status, String code, String message) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.name(),
                errorCode.getMessage()
        );
    }
    // message만 바꾸고 싶을 때
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.name(),
                message
        );
    }
}


