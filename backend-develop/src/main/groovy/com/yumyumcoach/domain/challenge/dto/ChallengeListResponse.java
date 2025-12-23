package com.yumyumcoach.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 챌린지 목록 전체 응답 DTO.
 * GET /api/challenges?month=YYYY-MM
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeListResponse {

    /**
     * 조회 기준 월 (YYYY-MM 형식 문자열)
     * 예) "2025-12"
     */
    private String month;

    /**
     * 해당 월에 노출되는 챌린지 목록
     */
    private List<ChallengeResponse> challenges;
}
