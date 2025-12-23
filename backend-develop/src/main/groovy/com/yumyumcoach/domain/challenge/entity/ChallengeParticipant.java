package com.yumyumcoach.domain.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeParticipant {

    /**
     * 챌린지 ID (FK, challenges.id)
     * PK 복합 키의 일부 (challenge_id, email)
     */
    private Long challengeId;

    /**
     * 참여한 사용자 이메일 (FK, accounts.email)
     * PK 복합 키의 일부 (challenge_id, email)
     */
    private String email;

    /**
     * 이 사용자가 챌린지에 참여한 시각
     */
    private LocalDateTime joinedAt;

    /**
     * 참여 상태
     * 예) joined / completed / left
     */
    private String status;

    /**
     * 선택한 난이도 코드
     * 예) BEGINNER / INTERMEDIATE / ADVANCED
     */
    private String difficultyCode;

    /**
     * 이 유저 기준 최소 성공해야 하는 일수
     * 예) 초급 8일, 중급 12일, 고급 20일 등
     */
    private Integer requiredSuccessDays;

    /**
     * 하루 기준 목표 값
     * 예) 단백질 g, 운동 분 등
     * 단순 "기록만 있으면 성공"인 챌린지는 null 일 수 있음
     */
    private Double dailyTargetValue;

    /**
     * 진행률(0.0 ~ 100.0)
     * 성공한 일수 / requiredSuccessDays * 100 으로 계산한 값을 캐싱
     */
    private Double progressPercentage;

    /**
     * 성공한 일수
     * 조건을 만족한 날짜 수를 캐싱해두는 값
     */
    private Integer successDays;

    /**
     * 마지막으로 진행률/성공 일수를 계산한 시각
     */
    private LocalDateTime lastEvaluatedAt;

    /**
     * 챌린지를 완료했거나, 중도에 나간 시각
     * status가 completed/left로 바뀔 때 함께 세팅
     */
    private LocalDateTime completedAt;

    /**
     * 새로 챌린지에 참여할 때 사용할 팩토리 메서드.
     * joinedAt 은 현재 시각(LocalDateTime.now())으로 세팅하고,
     * status 는 "joined", progressPercentage 는 0.0 으로 시작한다.
     *
     * @param challengeId         챌린지 ID
     * @param email               사용자 이메일
     * @param difficultyCode      선택한 난이도 코드
     * @param requiredSuccessDays 이 유저 기준 최소 성공 일수
     * @param dailyTargetValue    하루 기준 목표 값 (단순 일수형이면 null)
     * @return 새로 생성된 ChallengeParticipant
     */
    public static ChallengeParticipant newJoin(
            Long challengeId,
            String email,
            String difficultyCode,
            int requiredSuccessDays,
            Double dailyTargetValue
    ) {
        return ChallengeParticipant.builder()
                .challengeId(challengeId)
                .email(email)
                .joinedAt(LocalDateTime.now())
                .status("joined")
                .difficultyCode(difficultyCode)
                .requiredSuccessDays(requiredSuccessDays)
                .dailyTargetValue(dailyTargetValue)
                .progressPercentage(0.0)
                .successDays(0)
                .lastEvaluatedAt(null)
                .completedAt(null)
                .build();
    }

    /**
     * 챌린지를 성공적으로 완료했을 때 상태/시간/진행률을 업데이트한다.
     *
     * @param completedAt 완료 시각
     */
    public void complete(LocalDateTime completedAt) {
        this.status = "completed";
        this.completedAt = completedAt;
        this.progressPercentage = 100.0;
    }

    /**
     * 챌린지에서 중도 탈퇴할 때 상태/시간을 업데이트한다.
     *
     * @param leftAt 나간 시각
     */
    public void leave(LocalDateTime leftAt) {
        this.status = "left";
        this.completedAt = leftAt;
    }

    /**
     * 진행률과 마지막 평가 시각을 갱신한다.
     *
     * @param successDays        성공한 일수
     * @param progressPercentage 새 진행률 (0.0 ~ 100.0)
     * @param evaluatedAt        평가 시각
     */
    public void updateProgress(Integer successDays, Double progressPercentage, LocalDateTime evaluatedAt) {
        this.successDays = successDays;
        this.progressPercentage = progressPercentage;
        this.lastEvaluatedAt = evaluatedAt;
    }
}

