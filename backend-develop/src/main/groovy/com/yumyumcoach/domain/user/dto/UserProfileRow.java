package com.yumyumcoach.domain.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRow {
    private Long userId;
    private String email;
    private String username;
    private String introduction;
    private String profileImageUrl;

    private Long currentTitleId;      // nullable
    private String currentTitleName;  // nullable
    private String currentTitleIconEmoji; // nullable
}
