package com.yumyumcoach.domain.diet.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietFoodDto {

    private Long id;
    private Long dietId;

    private Long foodId;
    private String foodName;

    private Double serveCount;

    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;

    private Integer orderIndex;
}


