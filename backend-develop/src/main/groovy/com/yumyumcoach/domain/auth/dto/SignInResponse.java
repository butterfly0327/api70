package com.yumyumcoach.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
로그인 응답 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long accessTokenExpiresIn; // access token 만료시간(단위: 밀리초)
    private long refreshTokenExpiresIn; // refresh token 만료시간(단위: 밀리 초)
    private UserInfo userInfo;
}
