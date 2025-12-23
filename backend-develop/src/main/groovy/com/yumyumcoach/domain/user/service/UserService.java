package com.yumyumcoach.domain.user.service;

import com.yumyumcoach.domain.auth.entity.Account;
import com.yumyumcoach.domain.auth.mapper.AccountMapper;
import com.yumyumcoach.domain.title.dto.MyTitleItemResponse;
import com.yumyumcoach.domain.title.dto.MyTitleResponse;
import com.yumyumcoach.domain.user.dto.MyPageResponse;
import com.yumyumcoach.domain.user.dto.UpdateMyBasicInfoRequest;
import com.yumyumcoach.domain.user.dto.UpdateMyHealthInfoRequest;
import com.yumyumcoach.domain.user.dto.UserProfileResponse;
import com.yumyumcoach.domain.user.entity.Profile;
import com.yumyumcoach.domain.user.mapper.FollowMapper;
import com.yumyumcoach.domain.user.mapper.ProfileMapper;
import com.yumyumcoach.domain.title.mapper.TitleMapper;
import com.yumyumcoach.global.common.CdnUrlResolver;
import com.yumyumcoach.global.common.CredentialValidator;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final AccountMapper accountMapper;
    private final ProfileMapper profileMapper;
    private final FollowMapper followMapper;
    private final TitleMapper titleMapper;
    private final CdnUrlResolver cdnUrlResolver;

    public MyPageResponse getMyPage(String email) {

        Account account = accountMapper.findByEmail(email);
        if (account == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Long userId = accountMapper.findIdByEmail(email);

        Profile profile = profileMapper.findByEmail(email);
        if (profile == null) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        long followers = followMapper.countFollowers(email);
        long followings = followMapper.countFollowings(email);

        MyTitleResponse current = titleMapper.findCurrentTitle(email);

        Long currentTitleId = null;
        String currentTitleName = null;

        if (current != null) {
            currentTitleId = current.getCurrentTitleId();
            currentTitleName = current.getCurrentTitleName();
        }
        List<MyTitleItemResponse> myTitles = titleMapper.findMyTitles(email);

        return MyPageResponse.builder()
                .basic(MyPageResponse.Basic.builder()
                        .userId(userId)
                        .email(email)
                        .username(account.getUsername())
                        .profileImageUrl(cdnUrlResolver.resolve(profile.getProfileImageUrl()))
                        .introduction(profile.getIntroduction())
                        .build())
                .health(MyPageResponse.Health.builder()
                        .birthDate(profile.getBirthDate())
                        .height(profile.getHeight())
                        .weight(profile.getCurrentWeight())
                        .goalWeight(profile.getTargetWeight())
                        .hasDiabetes(profile.getHasDiabetes())
                        .hasHypertension(profile.getHasHypertension())
                        .hasHyperlipidemia(profile.getHasHyperlipidemia())
                        .otherDisease(profile.getOtherDisease())
                        .goal(profile.getGoal())
                        .activityLevel(profile.getActivityLevel())
                        .build())
                .badges(MyPageResponse.Badges.builder()
                        .currentTitleId(currentTitleId)
                        .currentTitleName(currentTitleName)
                        .titles(myTitles)
                        .build())
                .follow(MyPageResponse.Follow.builder()
                        .followersCount(followers)
                        .followingsCount(followings)
                        .build())
                .build();
    }

    @Transactional
    public MyPageResponse.Basic updateMyBasicInfo(String email, UpdateMyBasicInfoRequest req) {

        if (req == null || !req.hasAnyValue()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        Profile profile = profileMapper.findByEmail(email);
        if (profile == null) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        Account account = accountMapper.findByEmail(email);
        if (account == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        if (req.getUsername() != null) {
            String newUsername = req.getUsername().trim();
            CredentialValidator.validateUsername(newUsername);
            if (newUsername.isBlank()) {
                throw new BusinessException(ErrorCode.AUTH_INVALID_USERNAME_FORMAT);
            }

            if (accountMapper.existsByUsername(newUsername)) {
                throw new BusinessException(ErrorCode.AUTH_USERNAME_ALREADY_EXISTS);
            }

            accountMapper.updateUsername(email, newUsername);
        }

        boolean needProfileUpdate =
                req.getProfileImageUrl() != null || req.getIntroduction() != null;

        if (needProfileUpdate) {
            Profile patch = Profile.builder()
                    .email(email)
                    .profileImageUrl(req.getProfileImageUrl())
                    .introduction(req.getIntroduction())
                    .build();
            profileMapper.updateBasic(patch);
        }

        Profile updatedProfile = profileMapper.findByEmail(email);
        Long userId = accountMapper.findIdByEmail(email);
        Account updatedAccount = accountMapper.findByEmail(email);

        return MyPageResponse.Basic.builder()
                .userId(userId)
                .email(email)
                .username(updatedAccount.getUsername())
                .profileImageUrl(cdnUrlResolver.resolve(updatedProfile.getProfileImageUrl()))
                .introduction(updatedProfile.getIntroduction())
                .build();
    }

    @Transactional
    public MyPageResponse.Health updateMyHealthInfo(String email, UpdateMyHealthInfoRequest req) {
        if (req == null || !req.hasAnyValue()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        Profile profile = profileMapper.findByEmail(email);
        if (profile == null) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        Profile patch = Profile.builder()
                .email(email)
                .birthDate(req.getBirthDate())
                .height(req.getHeight())
                .currentWeight(req.getWeight())
                .targetWeight(req.getGoalWeight())
                .hasDiabetes(req.getHasDiabetes())
                .hasHypertension(req.getHasHypertension())
                .hasHyperlipidemia(req.getHasHyperlipidemia())
                .otherDisease(req.getOtherDisease())
                .goal(req.getGoal())
                .activityLevel(req.getActivityLevel())
                .build();

        profileMapper.updateHealth(patch);

        Profile updated = profileMapper.findByEmail(email);

        return MyPageResponse.Health.builder()
                .birthDate(updated.getBirthDate())
                .height(updated.getHeight())
                .weight(updated.getCurrentWeight())
                .goalWeight(updated.getTargetWeight())
                .hasDiabetes(updated.getHasDiabetes())
                .hasHypertension(updated.getHasHypertension())
                .hasHyperlipidemia(updated.getHasHyperlipidemia())
                .otherDisease(updated.getOtherDisease())
                .goal(updated.getGoal())
                .activityLevel(updated.getActivityLevel())
                .build();
    }

    @Transactional
    public MyTitleResponse selectMyTitle(String email, Long titleId) {

        if (titleId == null) {
            int updated = profileMapper.updateDisplayTitle(email, null);
            if (updated == 0) {
                throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
            }
            // 해제면 current null 내려주게
            return MyTitleResponse.builder()
                    .currentTitleId(null)
                    .currentTitleName(null)
                    .currentTitleEmoji(null)
                    .build();
            // 또는 titleMapper.findCurrentTitle(email)가 null-safe면 그걸 써도 됨
        }

        if (!titleMapper.ownsTitle(email, titleId)) {
            throw new BusinessException(ErrorCode.USER_TITLE_NOT_FOUND);
        }

        int updated = profileMapper.updateDisplayTitle(email, titleId);
        if (updated == 0) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        return titleMapper.findCurrentTitle(email);
    }

    public UserProfileResponse getUserProfile(String viewerEmail, Long userId) {

        // 1) userId -> targetEmail
        String targetEmail = accountMapper.findEmailById(userId);
        if (targetEmail == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2) 계정 / 프로필 조회
        Account targetAccount = accountMapper.findByEmail(targetEmail);
        if (targetAccount == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Profile targetProfile = profileMapper.findByEmail(targetEmail);
        if (targetProfile == null) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        // 3) 팔로우 요약 + isFollowing
        long followers = followMapper.countFollowers(targetEmail);
        long followings = followMapper.countFollowings(targetEmail);

        boolean isFollowing = false;
        if (viewerEmail != null && !viewerEmail.equals(targetEmail)) {
            isFollowing = followMapper.exists(viewerEmail, targetEmail);
        }

        // 4) 대표 타이틀(없을 수 있음) + 보유 타이틀 목록
        MyTitleResponse current = titleMapper.findCurrentTitle(targetEmail); // null 가능
        List<MyTitleItemResponse> titles = titleMapper.findMyTitles(targetEmail);

        // 5) 응답 조립 (email 없이)
        return UserProfileResponse.builder()
                .basic(UserProfileResponse.Basic.builder()
                        .userId(userId)
                        .username(targetAccount.getUsername())
                        .profileImageUrl(cdnUrlResolver.resolve(targetProfile.getProfileImageUrl()))
                        .introduction(targetProfile.getIntroduction())
                        .build())
                .follow(UserProfileResponse.Follow.builder()
                        .followersCount(followers)
                        .followingsCount(followings)
                        .isFollowing(isFollowing)
                        .build())
                .badges(UserProfileResponse.Badges.builder()
                        .currentTitleId(current != null ? current.getCurrentTitleId() : null)
                        .currentTitleName(current != null ? current.getCurrentTitleName() : null)
                        .currentIconEmoji(current != null ? current.getCurrentTitleEmoji() : null)
                        .titles(titles)
                        .build())
                .build();
    }
}
