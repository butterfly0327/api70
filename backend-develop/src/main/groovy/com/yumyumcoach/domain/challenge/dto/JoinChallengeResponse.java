package com.yumyumcoach.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 챌린지 참여 성공 시 응답 DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinChallengeResponse {

    /**
     * 챌린지 ID
     */
    private Long challengeId;

    /**
     * 챌린지 제목 (이름)
     */
    private String title;

    /**
     * 참여 여부 (성공 시 항상 true)
     */
    private Boolean joined;

    /**
     * 참여 시각
     * 예) "2025-11-20T09:00:00"
     */
    private String joinedAt;

    /**
     * 선택한 난이도 코드
     * 예) "INTERMEDIATE"
     */
    private String difficultyCode;

    /**
     * 나에게 요구되는 최소 성공 일수
     * 예) 12
     */
    private Integer requiredSuccessDays;

    /**
     * 하루 기준 목표 값
     * 예) 단백질 g, 운동 분 등
     * 단순 일수 챌린지는 null 일 수 있음
     */
    private Double dailyTargetValue;

    /**
     * 나의 챌린지 기간 시작일 (yyyy-MM-dd)
     */
    private String myStartDate;

    /**
     * 나의 챌린지 기간 종료일 (yyyy-MM-dd)
     */
    private String myEndDate;
}

