package com.yumyumcoach.domain.challenge.service;

import com.yumyumcoach.domain.challenge.dto.*;
import com.yumyumcoach.domain.challenge.entity.Challenge;
import com.yumyumcoach.domain.challenge.entity.ChallengeParticipant;
import com.yumyumcoach.domain.challenge.entity.ChallengeRule;
import com.yumyumcoach.domain.challenge.mapper.ChallengeMapper;
import com.yumyumcoach.domain.challenge.mapper.ChallengeParticipantMapper;
import com.yumyumcoach.domain.challenge.mapper.ChallengeRuleMapper;
import com.yumyumcoach.domain.challenge.model.DifficultyCode;
import com.yumyumcoach.domain.challenge.model.GoalType;
import com.yumyumcoach.global.common.CdnUrlResolver;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

/**
 * 공용 챌린지 도메인 서비스.
 * - 목록/상세 조회
 * - 참여/나가기
 *
 * 정책:
 * - month는 "옵션" (없으면 이번 달)
 * - 다음 달 포함/월말 로직 없음
 * - recruitStartDate / recruitEndDate 는 항상 존재한다고 가정
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeParticipantMapper challengeParticipantMapper;
    private final ChallengeRuleMapper challengeRuleMapper;
    private final CdnUrlResolver cdnUrlResolver;

    /**
     * 특정 월 기준 챌린지 목록 조회
     * - month 없으면 이번 달
     * - "그 달에 속하는 챌린지"만 조회
     */
    public ChallengeListResponse getChallenges(String month, String email) {
        YearMonth targetYm = parseMonthOrNow(month);

        LocalDate startDate = targetYm.atDay(1);
        LocalDate endDate = targetYm.atEndOfMonth();

        List<Challenge> challenges = challengeMapper.findByPeriod(startDate, endDate);

        List<ChallengeResponse> challengeResponses = challenges.stream()
                .map(challenge -> toChallengeResponseForList(challenge, email))
                .toList();

        return ChallengeListResponse.builder()
                .month(targetYm.toString())
                .challenges(challengeResponses)
                .build();
    }

    /**
     * 챌린지 상세 조회
     */
    public ChallengeResponse getChallengeDetail(Long challengeId, String email) {
        Challenge challenge = challengeMapper.findById(challengeId);
        if (challenge == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND);
        }

        ChallengeParticipant participant =
                challengeParticipantMapper.findByChallengeIdAndEmail(challengeId, email);

        List<ChallengeDifficultyOptionResponse> difficultyOptions =
                challengeRuleMapper.findDifficultyOptionsByChallengeId(challengeId);

        return toChallengeResponseForDetail(challenge, participant, difficultyOptions);
    }

    /**
     * 챌린지 참여(사전 신청 포함)
     * - 모집기간 내에서만 가능
     * - 이미 참여 이력 있으면 불가
     */
    @Transactional
    public JoinChallengeResponse joinChallenge(Long challengeId, String email, JoinChallengeRequest request) {
        Challenge challenge = challengeMapper.findById(challengeId);
        if (challenge == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND);
        }

        // 모집기간 체크
        LocalDate today = LocalDate.now();
        if (!challenge.isRecruitingOn(today)) {
            throw new BusinessException(ErrorCode.CHALLENGE_JOIN_NOT_ALLOWED);
        }

        // 이미 참여 체크
        int existing = challengeParticipantMapper.existsByChallengeIdAndEmail(challengeId, email);
        if (existing > 0) {
            throw new BusinessException(ErrorCode.CHALLENGE_ALREADY_JOINED);
        }

        DifficultyCode difficultyCode = parseDifficulty(request == null ? null : request.getDifficultyCode());
        GoalType goalType = parseGoalType(challenge.getGoalType());

        ChallengeRule rule = challengeRuleMapper.findByChallengeIdAndDifficulty(
                challengeId,
                difficultyCode.getCode()
        );
        if (rule == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_RULE_NOT_FOUND);
        }

        int requiredSuccessDays = rule.getRequiredSuccessDays();

        Double dailyTargetValue = null;
        if (goalType != GoalType.DAY_COUNT_SIMPLE) {
            dailyTargetValue = rule.getDailyTargetValue();
            if (dailyTargetValue == null) {
                throw new BusinessException(ErrorCode.CHALLENGE_RULE_INVALID);
            }
        }

        ChallengeParticipant participant = ChallengeParticipant.newJoin(
                challengeId,
                email,
                difficultyCode.getCode(),
                requiredSuccessDays,
                dailyTargetValue
        );
        challengeParticipantMapper.insert(participant);

        return JoinChallengeResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getName())
                .joined(true)
                .joinedAt(participant.getJoinedAt().toString())
                .difficultyCode(difficultyCode.getCode())
                .requiredSuccessDays(requiredSuccessDays)
                .dailyTargetValue(dailyTargetValue)
                .myStartDate(challenge.getStartDate().toString())
                .myEndDate(challenge.getEndDate().toString())
                .build();
    }

    /**
     * 챌린지 나가기
     * - 시작 전: 참여 취소(delete)
     * - 시작 후: 중도 탈퇴(status=left)
     */
    @Transactional
    public LeaveChallengeResponse leaveChallenge(Long challengeId, String email) {
        Challenge challenge = challengeMapper.findById(challengeId);
        if (challenge == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND);
        }

        ChallengeParticipant existing =
                challengeParticipantMapper.findByChallengeIdAndEmail(challengeId, email);

        if (existing == null) {
            throw new BusinessException(ErrorCode.CHALLENGE_JOIN_NOT_FOUND);
        }
        if ("left".equalsIgnoreCase(existing.getStatus())) {
            throw new BusinessException(ErrorCode.CHALLENGE_ALREADY_LEFT);
        }

        LocalDate today = LocalDate.now();
        boolean isBeforeStart = today.isBefore(challenge.getStartDate());
        LocalDateTime leftAt = LocalDateTime.now();

        if (isBeforeStart) {
            challengeParticipantMapper.deleteByChallengeIdAndEmail(challengeId, email);
        } else {
            existing.leave(leftAt);
            challengeParticipantMapper.updateStatus(challengeId, email, "left", leftAt);
        }

        return LeaveChallengeResponse.builder()
                .challengeId(challengeId)
                .left(true)
                .leftAt(leftAt.toString())
                .build();
    }

    /**
     * 목록용 응답 변환
     * - 상세용(난이도 옵션)은 안 내림
     */
    private ChallengeResponse toChallengeResponseForList(Challenge challenge, String email) {
        ChallengeParticipant participant =
                challengeParticipantMapper.findByChallengeIdAndEmail(challenge.getId(), email);

        int participantsCount = challengeParticipantMapper.countByChallengeId(challenge.getId());

        Integer successDays = null;
        Double progressPercentage = null;
        String selectedDifficulty = null;
        Integer requiredSuccessDays = null;
        Double dailyTargetValue = null;
        boolean isJoined = false;

        if (participant != null && !"left".equalsIgnoreCase(participant.getStatus())) {
            isJoined = true;
            successDays = participant.getSuccessDays();
            progressPercentage = participant.getProgressPercentage();
            selectedDifficulty = participant.getDifficultyCode();
            requiredSuccessDays = participant.getRequiredSuccessDays();
            dailyTargetValue = participant.getDailyTargetValue();
        }

        return ChallengeResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getName())
                .shortDescription(challenge.getShortDescription())
                .goalSummary(challenge.getGoalSummary())
                .ruleDescription(null)
                .imageUrl(cdnUrlResolver.resolve(challenge.getImageUrl()))
                .type(challenge.getChallengeType())
                .goalType(challenge.getGoalType())
                .recruitStartDate(challenge.getRecruitStartDate().toString())
                .recruitEndDate(challenge.getRecruitEndDate().toString())
                .startDate(challenge.getStartDate().toString())
                .endDate(challenge.getEndDate().toString())
                .participantsCount(participantsCount)
                .isJoined(isJoined)
                .selectedDifficulty(selectedDifficulty)
                .requiredSuccessDays(requiredSuccessDays)
                .dailyTargetValue(dailyTargetValue)
                .successDays(successDays)
                .progressPercentage(progressPercentage)
                .difficultyOptions(null)
                .build();
    }

    /**
     * 상세용 응답 변환
     * - ✅ 난이도 3개 옵션 내려줌(프론트에서 라디오/카드로 선택)
     */
    private ChallengeResponse toChallengeResponseForDetail(Challenge challenge, ChallengeParticipant participant, List<ChallengeDifficultyOptionResponse> difficultyOptions) {
        int participantsCount = challengeParticipantMapper.countByChallengeId(challenge.getId());

        Integer successDays = null;
        Double progressPercentage = null;
        String selectedDifficulty = null;
        Integer requiredSuccessDays = null;
        Double dailyTargetValue = null;
        boolean isJoined = false;

        if (participant != null && !"left".equalsIgnoreCase(participant.getStatus())) {
            isJoined = true;
            successDays = participant.getSuccessDays();
            progressPercentage = participant.getProgressPercentage();
            selectedDifficulty = participant.getDifficultyCode();
            requiredSuccessDays = participant.getRequiredSuccessDays();
            dailyTargetValue = participant.getDailyTargetValue();
        }

        return ChallengeResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getName())
                .shortDescription(challenge.getShortDescription())
                .goalSummary(challenge.getGoalSummary())
                .ruleDescription(challenge.getRuleDescription())
                .imageUrl(cdnUrlResolver.resolve(challenge.getImageUrl()))
                .type(challenge.getChallengeType())
                .goalType(challenge.getGoalType())
                .recruitStartDate(challenge.getRecruitStartDate().toString())
                .recruitEndDate(challenge.getRecruitEndDate().toString())
                .startDate(challenge.getStartDate().toString())
                .endDate(challenge.getEndDate().toString())
                .participantsCount(participantsCount)
                .isJoined(isJoined)
                .selectedDifficulty(selectedDifficulty)
                .requiredSuccessDays(requiredSuccessDays)
                .dailyTargetValue(dailyTargetValue)
                .successDays(successDays)
                .progressPercentage(progressPercentage)
                .difficultyOptions(difficultyOptions)
                .build();
    }

    private YearMonth parseMonthOrNow(String month) {
        if (month == null || month.isBlank()) return YearMonth.now();
        try {
            return YearMonth.parse(month);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "month 형식이 올바르지 않습니다. (yyyy-MM)");
        }
    }

    private DifficultyCode parseDifficulty(String raw) {
        try {
            return DifficultyCode.from(raw);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "difficultyCode가 올바르지 않습니다.");
        }
    }

    private GoalType parseGoalType(String raw) {
        try {
            return GoalType.from(raw);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "goalType이 올바르지 않습니다.");
        }
    }
}
