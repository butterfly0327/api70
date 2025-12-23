package com.yumyumcoach.domain.challenge.mapper;

import com.yumyumcoach.domain.challenge.entity.Challenge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 공용 챌린지(challenges) 테이블용 MyBatis Mapper.
 */
@Mapper
public interface ChallengeMapper {

    /**
     * 특정 기간에 시작되는 챌린지 목록을 조회한다.
     * - 이번 달 챌린지, 다음 달 챌린지 등 조회에 사용.
     *
     * @param startDate 조회 시작일(포함)
     * @param endDate   조회 종료일(포함)
     * @return 기간에 속한 챌린지 목록
     */
    List<Challenge> findByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 챌린지 ID로 단일 챌린지를 조회한다.
     *
     * @param challengeId 챌린지 ID
     * @return 해당 챌린지, 없으면 null
     */
    Challenge findById(@Param("challengeId") Long challengeId);

    /**
     * 특정 챌린지에 참여 중인 인원 수를 조회한다.
     *
     * @param challengeId 챌린지 ID
     * @return 참여자 수
     */
    int countParticipants(@Param("challengeId") Long challengeId);
}
