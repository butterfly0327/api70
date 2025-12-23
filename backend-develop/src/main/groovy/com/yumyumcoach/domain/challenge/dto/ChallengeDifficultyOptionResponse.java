package com.yumyumcoach.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDifficultyOptionResponse {
    private String difficultyCode;       // BEGINNER / INTERMEDIATE / ADVANCED
    private Integer requiredSuccessDays; // 예: 8, 12, 20
    private Double dailyTargetValue;     // DAY_COUNT_SIMPLE면 null
    private Long rewardTitleId;
    private String rewardTitleName;
    private String rewardTitleDescription;
    private String rewardTitleIconEmoji;
}
