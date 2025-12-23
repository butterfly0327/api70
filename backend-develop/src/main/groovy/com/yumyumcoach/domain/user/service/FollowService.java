package com.yumyumcoach.domain.user.service;

import com.yumyumcoach.domain.auth.mapper.AccountMapper;
import com.yumyumcoach.domain.user.dto.*;
import com.yumyumcoach.domain.user.entity.Follow;
import com.yumyumcoach.domain.user.mapper.FollowMapper;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final AccountMapper accountMapper; // findEmailById 사용
    private final FollowMapper followMapper;

    /**
     * 팔로우하기
     */
    @Transactional
    public FollowUserResponse followUser(String myEmail, Long targetUserId) {

        String targetEmail = accountMapper.findEmailById(targetUserId);
        if (targetEmail == null) {
            throw new BusinessException(ErrorCode.FOLLOW_TARGET_NOT_FOUND);
        }

        if (myEmail.equals(targetEmail)) {
            throw new BusinessException(ErrorCode.FOLLOW_INVALID_REQUEST);
        }

        if (followMapper.exists(myEmail, targetEmail)) {
            throw new BusinessException(ErrorCode.FOLLOW_ALREADY_EXISTS);
        }

        LocalDateTime now = LocalDateTime.now();

        followMapper.insert(Follow.builder()
                .followerEmail(myEmail)
                .followeeEmail(targetEmail)
                .followedAt(now)
                .build());

        return FollowUserResponse.builder()
                .targetUserId(targetUserId)
                .following(true)
                .followedAt(now)
                .build();
    }

    /**
     * 팔로우 취소
     */
    @Transactional
    public UnfollowUserResponse unfollowUser(String myEmail, Long targetUserId) {

        String targetEmail = accountMapper.findEmailById(targetUserId);
        if (targetEmail == null) {
            throw new BusinessException(ErrorCode.FOLLOW_TARGET_NOT_FOUND);
        }

        if (myEmail.equals(targetEmail)) {
            throw new BusinessException(ErrorCode.FOLLOW_INVALID_REQUEST);
        }

        int deleted = followMapper.delete(myEmail, targetEmail);
        if (deleted == 0) {
            throw new BusinessException(ErrorCode.FOLLOW_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();

        return UnfollowUserResponse.builder()
                .targetUserId(targetUserId)
                .following(false)
                .unfollowedAt(now)
                .build();
    }

    /**
     * 내가 팔로우하는 유저 목록
     */
    public MyFollowingsResponse getMyFollowings(String myEmail) {
        long total = followMapper.countFollowings(myEmail);
        List<MyFollowingsResponse.User> users = followMapper.findMyFollowings(myEmail);

        return MyFollowingsResponse.builder()
                .totalCount(total)
                .users(users)
                .build();
    }

    /**
     * 나를 팔로우하는 유저 목록
     */
    public MyFollowersResponse getMyFollowers(String myEmail) {
        long total = followMapper.countFollowers(myEmail);
        List<MyFollowersResponse.User> users = followMapper.findMyFollowers(myEmail);

        return MyFollowersResponse.builder()
                .totalCount(total)
                .users(users)
                .build();
    }
}

