package com.yumyumcoach.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 챌린지 나가기 응답 DTO.
 * DELETE /api/challenges/{challengeId}/leave
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveChallengeResponse {

    /**
     * 챌린지 ID
     */
    private Long challengeId;

    /**
     * 나가기 처리 여부
     * 성공 시 true
     */
    private Boolean left;

    /**
     * 나간 시각
     * 예) "2025-11-25T18:00:00"
     */
    private String leftAt;
}

