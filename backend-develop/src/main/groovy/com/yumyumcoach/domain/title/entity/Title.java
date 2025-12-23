package com.yumyumcoach.domain.title.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 타이틀(뱃지) 엔티티 (titles 테이블 매핑).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    /**
     * 타이틀 ID (PK, AUTO_INCREMENT)
     */
    private Long id;

    /**
     * 타이틀 이름
     */
    private String name;

    /**
     * 타이틀 설명
     */
    private String description;

    /**
     * 타이틀 이모지
     */
    private String iconEmoji;
}
