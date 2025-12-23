package com.yumyumcoach.domain.user.dto;

import lombok.*;

import java.util.List;

/**
 * 나를 팔로우하는 유저 목록 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyFollowersResponse {

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
        private boolean isFollowing;    // 내가 이 사람을 다시 팔로우하는지
    }
}

