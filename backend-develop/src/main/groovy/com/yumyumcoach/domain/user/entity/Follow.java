package com.yumyumcoach.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 팔로우 관계 엔티티 (follows 테이블 매핑).
 * - PK: (follower_email, followee_email)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    /**
     * 팔로우 하는 사람 이메일 - DB: follower_email
     */
    private String followerEmail;

    /**
     * 팔로우 당하는 사람 이메일 - DB: followee_email
     */
    private String followeeEmail;

    /**
     * 팔로우 시각 - DB: followed_at
     */
    private LocalDateTime followedAt;
}

