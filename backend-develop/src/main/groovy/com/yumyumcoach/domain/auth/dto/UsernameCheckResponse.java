package com.yumyumcoach.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
"/api/auth/check-username" API 의 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameCheckResponse {
    private String username;
    private boolean available;
}
