package com.yumyumcoach.domain.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 챌린지 참여 요청 DTO.
 * POST /api/challenges/{challengeId}/join
 */
@Getter
@NoArgsConstructor
public class JoinChallengeRequest {

    /**
     * 참여 시 선택한 난이도 코드
     * 예) "BEGINNER", "INTERMEDIATE", "ADVANCED"
     */
    private String difficultyCode;
}

