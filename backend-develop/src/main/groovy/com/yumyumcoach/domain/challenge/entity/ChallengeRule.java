package com.yumyumcoach.domain.challenge.entity;

import com.yumyumcoach.domain.title.entity.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRule {

    /**
     * 챌린지 ID (FK, challenges.id)
     */
    private Long challengeId;

    /**
     * 난이도 코드 (BEGINNER / INTERMEDIATE / ADVANCED)
     */
    private String difficultyCode;

    /**
     * 최소 성공해야 하는 일수
     */
    private Integer requiredSuccessDays;

    /**
     * 하루 목표 값
     * - DAY_COUNT_SIMPLE 타입인 경우 null 일 수 있다.
     * - PROTEIN_PER_DAY: g
     * - EXERCISE_MINUTES_PER_DAY: 분
     */
    private Double dailyTargetValue;

    /**
     * 난이도별 보상 타이틀 id
     */
    private Long rewardTitleId;
}
