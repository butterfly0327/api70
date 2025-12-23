package com.yumyumcoach.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/*
    - JWT 생성/검증 담당 도우미 클래스
    - AccessToken/RefreshToken 만들기
    - 토큰에서 이메일 같은 클레임 꺼내기
    - 유효성 검사
 */

@Component
public class JwtTokenProvider {
    private static final String TOKEN_TYPE = "Bearer";

    private final SecretKey key;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expire-time}") Duration accessExpiration,
            @Value("${jwt.refresh-token-expire-time}") Duration refreshExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessExpiration;
        this.refreshTokenExpiration = refreshExpiration;
    }

    public String createAccessToken(String email) {
        return createToken(email, accessTokenExpiration);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenExpiration);
    }

    // 토큰 유효성 검사(예외는 전역 예외처리에서 처리)
    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return true;
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpiration.toSeconds();
    }

    public long getRefreshTokenExpirationSeconds() {
        return refreshTokenExpiration.toSeconds();
    }

    // 토큰 생성 공통 메서드
    private String createToken(String email, Duration expireTime) {
        Date now = new Date();
        Date expiration = Date.from(Instant.now().plus(expireTime));

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
}
