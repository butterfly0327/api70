package com.yumyumcoach.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long accessTokenExpiresIn;
    private long refreshTokenExpiresIn;
}
