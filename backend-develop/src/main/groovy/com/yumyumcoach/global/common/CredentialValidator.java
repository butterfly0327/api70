package com.yumyumcoach.global.common;

import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;

import java.util.regex.Pattern;

public final class CredentialValidator {
    private CredentialValidator() {}

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[가-힣a-zA-Z0-9._]{2,12}$");

    public static void validateEmail(String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_EMAIL_FORMAT);
        }
    }

    public static void validateUsername(String username) {
        if (username == null || username.isBlank() || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_USERNAME_FORMAT);
        }
    }
}
