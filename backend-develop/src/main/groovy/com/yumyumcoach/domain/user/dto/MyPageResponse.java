package com.yumyumcoach.domain.user.dto;

import com.yumyumcoach.domain.title.dto.MyTitleItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 내 마이페이지 정보 조회 응답 DTO
 * - 기본/건강/뱃지/팔로우 요약을 한 번에 반환
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {

    private Basic basic;
    private Health health;
    private Badges badges;
    private Follow follow;

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Basic {
        private Long userId;          // accounts.id
        private String email;         // accounts.email
        private String username;      // accounts.username
        private String profileImageUrl; // profiles.profile_image_url (없으면 null)
        private String introduction;  // profiles.introduction (없으면 null)
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Health {
        private LocalDate birthDate;      // profiles.birth_date
        private Double height;            // profiles.height
        private Double weight;            // profiles.current_weight
        private Double goalWeight;        // profiles.target_weight
        private Boolean hasDiabetes;      // profiles.has_diabetes
        private Boolean hasHypertension;  // profiles.has_hypertension
        private Boolean hasHyperlipidemia;// profiles.has_hyperlipidemia
        private String otherDisease;      // profiles.other_disease
        private String goal;              // profiles.goal
        private String activityLevel;     // profiles.activity_level
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Badges {
        private Long currentTitleId;      // profiles.display_title_id
        private String currentTitleName;  // titles.name
        private List<MyTitleItemResponse> titles;   // account_titles 기준
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TitleItem {
        private Long titleId;       // titles.id
        private String name;        // titles.name
        private String description; // titles.description
        private String iconEmoji;
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Follow {
        private long followersCount;  // follows where followee_email = me
        private long followingsCount; // follows where follower_email = me
    }
}
