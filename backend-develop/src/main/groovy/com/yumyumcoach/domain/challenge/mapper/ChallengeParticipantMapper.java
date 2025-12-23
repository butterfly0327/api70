package com.yumyumcoach.domain.challenge.mapper;


import com.yumyumcoach.domain.challenge.entity.ChallengeParticipant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 공용 챌린지 참여 정보(challenge_participants) 테이블용 MyBatis Mapper.
 */
@Mapper
public interface ChallengeParticipantMapper {

    /**
     * 챌린지 ID와 이메일로 단일 참여 정보를 조회한다.
     *
     * @param challengeId 챌린지 ID
     * @param email       사용자 이메일
     * @return 참여 정보, 없으면 null
     */
    ChallengeParticipant findByChallengeIdAndEmail(@Param("challengeId") Long challengeId, @Param("email") String email);

    /**
     * 사용자가 해당 챌린지에 이미 참여 중인지 여부를 조회한다.
     *
     * @param challengeId 챌린지 ID
     * @param email       사용자 이메일
     * @return 이미 존재하면 1 이상, 아니면 0
     */
    int existsByChallengeIdAndEmail(@Param("challengeId") Long challengeId, @Param("email") String email);

    /**
     * 새로운 챌린지 참여 정보를 추가한다.
     *
     * @param participant 참여 엔티티
     * @return insert된 row 수
     */
    int insert(ChallengeParticipant participant);

    /**
     * 참여 상태를 변경한다.
     * 예) joined → completed
     *
     * @param challengeId 챌린지 ID
     * @param email       사용자 이메일
     * @param status      변경할 상태 값
     * @param completedAt 완료/탈퇴 시각 (nullable)
     * @return 업데이트된 row 수
     */
    int updateStatus(
            @Param("challengeId") Long challengeId,
            @Param("email") String email,
            @Param("status") String status,
            @Param("completedAt") LocalDateTime completedAt
    );

    /**
     * 진행률과 마지막 평가 시각을 갱신한다.
     *
     * @param challengeId        챌린지 ID
     * @param email              사용자 이메일
     * @param successDays        성공 일수
     * @param progressPercentage 진행률(0.0 ~ 100.0)
     * @param evaluatedAt        평가 시각
     * @return 업데이트된 row 수
     */
    int updateProgress(
            @Param("challengeId") Long challengeId,
            @Param("email") String email,
            @Param("successDays") Integer successDays,
            @Param("progressPercentage") Double progressPercentage,
            @Param("evaluatedAt") LocalDateTime evaluatedAt
    );

    /**
     * 챌린지 사전 신청을 취소한다.
     *
     * @param challengeId 챌린지 ID
     * @param email       사용자 이메일
     * @return 삭제된 row 수
     */
    int deleteByChallengeIdAndEmail(
            @Param("challengeId") Long challengeId,
            @Param("email") String email
    );

    /**
     * 특정 챌린지에 참여한 전체 인원 수를 조회한다.
     *
     * @param challengeId 챌린지 ID
     * @return 참여 인원 수
     */
    int countByChallengeId(@Param("challengeId") Long challengeId);

}
