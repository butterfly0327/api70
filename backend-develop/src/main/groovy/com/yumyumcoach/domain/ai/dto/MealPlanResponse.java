package com.yumyumcoach.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanResponse {
    private boolean generated;
    private Long planId;
    private LocalDate targetDate;
    private String dayOfWeekKr;
    private MealPlanItem breakfast;
    private MealPlanItem lunch;
    private MealPlanItem dinner;
    private Double totalCalories;
    private LocalDateTime generatedAt;
    private String rawText;
}
