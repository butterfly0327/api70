package com.yumyumcoach.domain.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

    /**
     * 챌린지 ID (PK, challenges.id)
     */
    private Long id;

    /**
     * 완료 시 부여되는 칭호 ID (FK, titles.id)
     * 없을 수 있음(null 가능)
     */
    private Long rewardTitleId;

    /**
     * 챌린지 이름
     * 예) 11월 단백질 챌린지
     */
    private String name;

    /**
     * 짧은 설명
     * 챌린지 카드 리스트 등에서 한 줄로 보여줄 요약
     */
    private String shortDescription;

    /**
     * 기본/추천 난이도 코드
     * 예) BEGINNER / INTERMEDIATE / ADVANCED
     */
    private String difficultyCode;

    /**
     * 목표 한 줄 요약
     * 예) 11월 중 12일 이상 단백질 목표 달성
     */
    private String goalSummary;

    /**
     * 상세 규칙 설명
     * 난이도별 조건, 유의 사항 등을 자유롭게 서술
     */
    private String ruleDescription;

    /**
     * 챌린지 타입 코드
     * 예) PUBLIC / SEASONAL / DISEASE_SPECIFIC 등
     */
    private String typeCode;

    /**
     * 챌린지 대표 이미지 URL
     */
    private String imageUrl;

    /**
     * 챌린지 타입
     * 예) PUBLIC(운영자), USER(사용자 생성)
     */
    private String challengeType;

    /**
     * 모집 시작일
     * 이 날짜부터 챌린지 참여 신청이 가능함
     */
    private LocalDate recruitStartDate;

    /**
     * 모집 종료일
     * 이 날짜까지만 챌린지 참여 신청이 가능함
     */
    private LocalDate recruitEndDate;

    /**
     * 챌린지 시작일
     * 해당 월의 시작일(예: 2025-11-01)
     */
    private LocalDate startDate;

    /**
     * 챌린지 종료일
     * 해당 월의 말일(예: 2025-11-30)
     */
    private LocalDate endDate;

    /**
     * 챌린지 활성화 여부
     * 관리용 플래그 (1/0 → true/false)
     */
    private Boolean isActive;

    /**
     * 목표 판정 타입
     * 예) DAY_COUNT_SIMPLE / PROTEIN_PER_DAY / EXERCISE_MINUTES_PER_DAY
     */
    private String goalType;

    /**
     * 주어진 날짜 기준으로, 모집 기간(recruitStartDate ~ recruitEndDate) 내에 있는지 여부를 반환한다.
     *
     * @param today 오늘 날짜
     * @return 모집 중이면 true, 아니면 false
     */
    public boolean isRecruitingOn(LocalDate today) {
        return (today.isEqual(recruitStartDate) || today.isAfter(recruitStartDate))
                && (today.isEqual(recruitEndDate) || today.isBefore(recruitEndDate));
    }

    /**
     * 주어진 날짜 기준으로, 챌린지가 진행 중(startDate ~ endDate)인지 여부를 반환한다.
     *
     * @param today 오늘 날짜
     * @return 진행 중이면 true, 아니면 false
     */
    public boolean isRunningOn(LocalDate today) {
        return (today.isEqual(startDate) || today.isAfter(startDate))
                && (today.isEqual(endDate) || today.isBefore(endDate));
    }
}

