package com.yumyumcoach.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
로그인 요청 (email, password)
 */

@Getter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
