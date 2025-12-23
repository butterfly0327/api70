package com.yumyumcoach.domain.user.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 팔로우 취소 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnfollowUserResponse {

    /**
     * 언팔로우 대상 userId (accounts.id)
     */
    private Long targetUserId;

    /**
     * 팔로우 여부 (항상 false)
     */
    private boolean following;

    /**
     * 언팔로우 시각
     */
    private LocalDateTime unfollowedAt;
}

