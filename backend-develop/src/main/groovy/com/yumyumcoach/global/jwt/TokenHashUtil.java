package com.yumyumcoach.global.jwt;

import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
refresh token 을 SHA-256 알고리즘 + 16진법 변환 방식을 이용하여 해시(hex) 로 변환
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenHashUtil {

    // 토큰 문자열을 받아 SHA-256 문자열을 반환
    @SneakyThrows(NoSuchAlgorithmException.class)
    public static String sha256Hex(String token) {

        // 잘못된 인자 예외처리
        if (token == null || token.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(hashed.length * 2);
        for (byte b : hashed) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

}
