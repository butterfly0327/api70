package com.yumyumcoach.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 공용 챌린지 응답 DTO.
 * - 목록 카드
 * - 상세 조회
 * 두 곳에서 모두 사용한다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

    // ====== 공용 챌린지 기본 정보 ======

    /**
     * 챌린지 ID
     */
    private Long challengeId;

    /**
     * 챌린지 제목 (이름)
     * 예) 11월 식단 기록 챌린지
     */
    private String title;

    /**
     * 짧은 설명
     * 예) 11월 한 달 중 목표 일수만큼 식단을 기록하면 성공
     */
    private String shortDescription;

    /**
     * 목표 한 줄 요약
     * 상세 화면 상단에 노출할 수 있는 문구
     */
    private String goalSummary;

    /**
     * 상세 규칙 설명
     * 난이도별 조건, 유의 사항 등 긴 설명 텍스트
     */
    private String ruleDescription;

    /**
     * 챌린지 대표 이미지 URL
     */
    private String imageUrl;

    /**
     * 챌린지 타입 코드
     * 예) "PUBLIC"(운영자 생성), "USER"(사용자 생성)
     */
    private String type;

    /**
     * 목표 판정 타입 코드
     * 예) "DAY_COUNT_SIMPLE", "PROTEIN_PER_DAY", "EXERCISE_MINUTES_PER_DAY"
     */
    private String goalType;

    /**
     * 챌린지 시작일 (yyyy-MM-dd)
     * 예) 2025-11-01
     */
    private String startDate;

    /**
     * 챌린지 종료일 (yyyy-MM-dd)
     * 예) 2025-11-30
     */
    private String endDate;

    /**
     * 챌린지 모집 시작일 (yyyy-MM-dd)
     * 예) 2025-10-01
     */
    private String recruitStartDate;

    /**
     * 챌린지 모집 종료일 (yyyy-MM-dd)
     * 예) 2025-10-30
     */
    private String recruitEndDate;

    /**
     * 현재 챌린지에 참여 중인 인원 수
     */
    private Integer participantsCount;

    // ====== 내 참여 정보 (참여 중일 때만 의미 있음) ======

    /**
     * 현재 사용자가 이 챌린지에 참여 중인지 여부
     */
    private Boolean isJoined;

    /**
     * 내가 선택한 난이도 코드
     * 예) "INTERMEDIATE"
     * 참여하지 않은 경우 null
     */
    private String selectedDifficulty;

    /**
     * 나에게 요구되는 최소 성공 일수
     * 예) 12
     * 참여하지 않은 경우 null
     */
    private Integer requiredSuccessDays;

    /**
     * 하루 기준 목표 값
     * 예) 단백질 g, 운동 분 등
     * 단순 일수 챌린지는 null 일 수 있음
     */
    private Double dailyTargetValue;

    /**
     * 현재까지 성공한 일수
     * 예) 5
     * 참여하지 않은 경우 null
     */
    private Integer successDays;

    /**
     * 현재 진행률 (0.0 ~ 100.0)
     * 예) 45.0
     * 참여하지 않은 경우 null
     */
    private Double progressPercentage;

    /**
     * 난이도별 칭호 (초급/중급/고급)
     */
    private List<ChallengeDifficultyOptionResponse> difficultyOptions;
}


