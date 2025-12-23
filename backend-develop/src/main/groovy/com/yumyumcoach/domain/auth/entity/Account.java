package com.yumyumcoach.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
DB 의 users 와 매핑되는 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String email;
    private String username;
    private String password;
}
