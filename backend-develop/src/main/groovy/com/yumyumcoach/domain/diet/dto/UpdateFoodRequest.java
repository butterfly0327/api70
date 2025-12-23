package com.yumyumcoach.domain.diet.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFoodRequest {

    private String name;

    @PositiveOrZero
    private Double carbohydrate;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double fat;

    @PositiveOrZero
    private Double calories;
}


