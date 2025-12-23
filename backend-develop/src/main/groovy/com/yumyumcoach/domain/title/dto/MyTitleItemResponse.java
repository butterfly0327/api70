package com.yumyumcoach.domain.title.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTitleItemResponse {
    private Long titleId;
    private String iconEmoji;
    private String name;
    private String description;

    private String difficultyName;      // 초급/중급/고급 (nullable)
    private LocalDateTime obtainedAt;
    private Long sourceChallengeId;
}
