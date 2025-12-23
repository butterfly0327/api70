package com.yumyumcoach.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignOutRequest {
    String refreshToken;
}
