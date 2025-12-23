package com.yumyumcoach.domain.user.dto;

import com.yumyumcoach.domain.title.dto.MyTitleItemResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Basic basic;
    private Follow follow;
    private Badges badges;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Basic {
        private Long userId;
        private String username;
        private String profileImageUrl;
        private String introduction;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Follow {
        private long followersCount;
        private long followingsCount;
        private boolean isFollowing;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Badges {
        private Long currentTitleId;     // null 가능
        private String currentTitleName; // null 가능
        private String currentIconEmoji; // null 가능

        private List<MyTitleItemResponse> titles;
    }
}

