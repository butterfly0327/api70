package com.yumyumcoach.domain.challenge.service;

import com.yumyumcoach.domain.challenge.entity.Challenge;
import com.yumyumcoach.domain.challenge.entity.ChallengeRule;
import com.yumyumcoach.domain.challenge.mapper.ChallengeRuleMapper;
import com.yumyumcoach.domain.challenge.model.DifficultyCode;
import com.yumyumcoach.domain.challenge.model.GoalType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * challenge_rules 테이블을 기반으로
 * 챌린지 + 난이도별 룰(최소 성공 일수, 하루 목표값)을 해석하는 헬퍼.
 */
@Component
@RequiredArgsConstructor
public class ChallengeRuleResolver {
    private final ChallengeRuleMapper challengeRuleMapper;

    /**
     * 최소 성공 일수를 조회한다.
     *
     * @param challenge     챌린지 엔티티
     * @param difficultyCode 난이도 코드
     * @return 최소 성공 일수
     */
    public int resolveRequiredSuccessDays(Challenge challenge, DifficultyCode difficultyCode) {
        ChallengeRule rule = challengeRuleMapper.findByChallengeIdAndDifficulty(
                challenge.getId(),
                difficultyCode.getCode()
        );
        if (rule == null) {
            // TODO: 공통 예외로 교체
            throw new IllegalStateException("챌린지 룰이 설정되지 않았습니다. challengeId="
                    + challenge.getId() + ", difficulty=" + difficultyCode.getCode());
        }
        return rule.getRequiredSuccessDays();
    }

    /**
     * 하루 목표값을 조회한다.
     *
     * @param challenge     챌린지 엔티티
     * @param goalType      목표 타입
     * @param difficultyCode 난이도 코드
     * @return 하루 목표값 (단순 일수 기준이면 null)
     */
    public Double resolveDailyTargetValue(Challenge challenge, GoalType goalType, DifficultyCode difficultyCode) {
        // DAY_COUNT_SIMPLE 은 "기록만 있으면 1일 인정"이라 별도의 dailyTargetValue 없음
        if (goalType == GoalType.DAY_COUNT_SIMPLE) {
            return null;
        }

        ChallengeRule rule = challengeRuleMapper.findByChallengeIdAndDifficulty(
                challenge.getId(),
                difficultyCode.getCode()
        );

        if (rule == null) {
            // TODO: 공통 예외로 교체
            throw new IllegalStateException("챌린지 룰이 설정되지 않았습니다. challengeId="
                    + challenge.getId() + ", difficulty=" + difficultyCode.getCode());
        }

        Double value = rule.getDailyTargetValue();
        if (value == null) {
            // PROTEIN_PER_DAY, EXERCISE_MINUTES_PER_DAY 등인데 값이 없으면 설정 누락으로 간주
            throw new IllegalStateException("하루 목표값이 설정되지 않았습니다. challengeId="
                    + challenge.getId() + ", difficulty=" + difficultyCode.getCode()
                    + ", goalType=" + goalType.getCode());
        }
        return value;
    }
}
