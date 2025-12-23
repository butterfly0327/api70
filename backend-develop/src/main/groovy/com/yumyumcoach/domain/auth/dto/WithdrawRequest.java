package com.yumyumcoach.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String refreshToken;
}
