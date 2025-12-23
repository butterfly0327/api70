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
public class AiMealPlan {
    private Long id;
    private String email;
    private LocalDate targetDate;
    private String weekdayKr;

    private String breakfastMenu;
    private Double breakfastCalories;
    private String breakfastComment;

    private String lunchMenu;
    private Double lunchCalories;
    private String lunchComment;

    private String dinnerMenu;
    private Double dinnerCalories;
    private String dinnerComment;

    private Double totalCalories;
    private String promptContext;
    private String rawResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
