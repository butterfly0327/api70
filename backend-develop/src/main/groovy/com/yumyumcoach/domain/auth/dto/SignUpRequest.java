package com.yumyumcoach.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String email;
    @NotBlank @Size(min = 2, max = 12)
    private String username;
    @NotBlank
    private String password;
}
