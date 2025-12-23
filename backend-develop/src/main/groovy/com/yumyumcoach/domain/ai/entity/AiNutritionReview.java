package com.yumyumcoach.domain.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiNutritionReview {
    private Long id;
    private String email;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private LocalDate evaluatedUntilDate;

    private String carbohydrateStatus;
    private String proteinStatus;
    private String fatStatus;
    private String calorieStatus;
    private String summaryText;

    private String promptContext;
    private String rawResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
