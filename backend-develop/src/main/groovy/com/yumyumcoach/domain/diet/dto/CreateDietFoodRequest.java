package com.yumyumcoach.domain.diet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDietFoodRequest {

    private Long foodId;       // 선택: 기존 음식 마스터 참조
    private String foodName;   // 직접 입력 시 사용

    @NotNull
    @Positive
    private Double serveCount; // 인분(serve_count)

    @PositiveOrZero
    private Double calories;

    @PositiveOrZero
    private Double carbs;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double fat;

    @NotNull
    @Positive
    private Integer orderIndex; // 식단 내 표시 순서
}

