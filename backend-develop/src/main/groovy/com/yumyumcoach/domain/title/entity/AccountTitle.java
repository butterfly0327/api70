package com.yumyumcoach.domain.title.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 계정-타이틀 보유 관계 엔티티 (account_titles 테이블 매핑).
 * - (email, title_id) UNIQUE
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTitle {

    /**
     * PK (AUTO_INCREMENT)
     */
    private Long id;

    /**
     * 보유 타이틀 ID (FK: titles.id)
     */
    private Long titleId;

    /**
     * 보유 계정 이메일 (FK: accounts.email)
     */
    private String email;

    /**
     * 획득 시각
     */
    private LocalDateTime obtainedAt;

    /**
     * 획득 출처 챌린지 ID (nullable)
     */
    private Long sourceChallengeId;
}
