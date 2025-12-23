package com.yumyumcoach.domain.title.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 내 대표 타이틀(뱃지) 응답 DTO
 * - 마이페이지의 현재 대표 타이틀 조회/설정 응답에 공통 사용
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTitleResponse {

    private Long currentTitleId;     // profiles.display_title_id
    private String currentTitleName; // titles.name
    private String currentTitleEmoji;
}

