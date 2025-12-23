package com.yumyumcoach.domain.challenge.service;

import com.yumyumcoach.domain.challenge.entity.Challenge;
import com.yumyumcoach.domain.challenge.entity.ChallengeParticipant;
import com.yumyumcoach.domain.challenge.mapper.ChallengeMapper;
import com.yumyumcoach.domain.challenge.mapper.ChallengeParticipantMapper;
import com.yumyumcoach.domain.challenge.model.GoalType;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 챌린지 참여자 관련 비즈니스 로직을 담당하는 서비스.
 * 진행률 / 성공 일수 평가 (successDays, progressPercentage 갱신)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeParticipantService {
    private final ChallengeMapper challengeMapper;
    private final ChallengeParticipantMapper challengeParticipantMapper;

    /**
     * 특정 챌린지에 참여 중인 한 명의 사용자의 진행률을 재평가한다.
     * TODO: successDays 계산 쿼리/로직 구현하기
     *
     * @param challengeId 챌린지 ID
     * @param email       사용자 이메일
     */
    @Transactional
    public void evaluateProgress(Long challengeId, String email) {
        // 1) 챌린지 / 참여 정보 조회
        Challenge challenge = challengeMapper.findById(challengeId);
        if (challenge == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND);
        }

        ChallengeParticipant participant = challengeParticipantMapper.findByChallengeIdAndEmail(challengeId, email);
        if (participant == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_JOIN_NOT_FOUND);
        }

        // 2) 목표 타입에 따라 분기 (실제 쿼리/계산은 다음 이슈에서 구현)
        // ✅ TODO(다음 스텝): goalType별로 successDays를 "기록 테이블"에서 계산해야 함
        // 지금 스텝에서는 최소한 "DB 업데이트 흐름"이 살아있게만 만든다.
        int successDays = participant.getSuccessDays() != null ? participant.getSuccessDays() : 0;

        int required = participant.getRequiredSuccessDays() != null ? participant.getRequiredSuccessDays() : 0;
        double progress = 0.0;
        if (required > 0) {
            progress = (successDays * 100.0) / required;
            if (progress > 100.0) progress = 100.0;
        }

        LocalDateTime now = LocalDateTime.now();

        // 3) 엔티티에 반영
        participant.updateProgress(successDays, progress, now);

        // 4. DB 반영
        challengeParticipantMapper.updateProgress(
                challengeId,
                email,
                successDays,
                progress,
                now
        );
    }
}

