package com.yumyumcoach.domain.challenge.controller;

import com.yumyumcoach.domain.challenge.dto.ChallengeListResponse;
import com.yumyumcoach.domain.challenge.dto.ChallengeResponse;
import com.yumyumcoach.domain.challenge.dto.JoinChallengeRequest;
import com.yumyumcoach.domain.challenge.dto.JoinChallengeResponse;
import com.yumyumcoach.domain.challenge.dto.LeaveChallengeResponse;
import com.yumyumcoach.domain.challenge.service.ChallengeService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Challenge 관련 컨트롤러.
 * - /api/challenges 하위 엔드포인트 담당
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 챌린지 목록 조회
     * 예) GET /api/challenges?month=2025-12
     */
    @GetMapping
    public ChallengeListResponse getChallenges(@RequestParam("month") String month) {
        String email = CurrentUser.email();
        return challengeService.getChallenges(month, email);
    }

    /**
     * 개별 챌린지 상세 조회
     * 예) GET /api/challenges/{challengeId}
     */
    @GetMapping("/{challengeId}")
    public ChallengeResponse getChallengeDetail(@PathVariable("challengeId") Long challengeId) {
        String email = CurrentUser.email();
        return challengeService.getChallengeDetail(challengeId, email);
    }

    /**
     * 챌린지 참여 (사전 신청 포함)
     * 예) POST /api/challenges/{challengeId}/join
     */
    @PostMapping("/{challengeId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public JoinChallengeResponse joinChallenge(
            @PathVariable("challengeId") Long challengeId,
            @RequestBody JoinChallengeRequest request
    ) {
        String email = CurrentUser.email();
        return challengeService.joinChallenge(challengeId, email, request);
    }

    /**
     * 챌린지 나가기
     * - 시작 전: 사전신청 취소
     * - 시작 후: 중도 탈퇴 (status = left)
     * 예) DELETE /api/challenges/{challengeId}/leave
     */
    @DeleteMapping("/{challengeId}/leave")
    public LeaveChallengeResponse leaveChallenge(@PathVariable("challengeId") Long challengeId) {
        String email = CurrentUser.email();
        return challengeService.leaveChallenge(challengeId, email);
    }
}

