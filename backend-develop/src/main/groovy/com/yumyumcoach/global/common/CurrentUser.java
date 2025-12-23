package com.yumyumcoach.global.common;

import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    private CurrentUser() {}

    public static String email() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        return (String) auth.getPrincipal();
    }
}
