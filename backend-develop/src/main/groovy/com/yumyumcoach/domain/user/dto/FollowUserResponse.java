package com.yumyumcoach.domain.user.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 팔로우하기 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUserResponse {

    /**
     * 팔로우 대상 userId (accounts.id)
     */
    private Long targetUserId;

    /**
     * 팔로우 여부 (항상 true)
     */
    private boolean following;

    /**
     * 팔로우 시각
     */
    private LocalDateTime followedAt;
}
