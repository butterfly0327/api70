package com.yumyumcoach.domain.user.dto;

import lombok.*;

import java.util.List;

/**
 * 내가 팔로우하는 유저 목록 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyFollowingsResponse {

    private long totalCount;
    private List<User> users;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private Long userId;            // accounts.id
        private String username;        // accounts.username
        private String profileImageUrl; // profiles.profile_image_url
        private String introduction;    // profiles.introduction
        private boolean isFollowingBack;// 상대도 나를 팔로우하는지
    }
}

