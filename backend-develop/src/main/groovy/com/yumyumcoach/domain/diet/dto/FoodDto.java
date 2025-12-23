package com.yumyumcoach.domain.diet.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDto {

    private Long id;
    private String name;
    private Double carbohydrate;
    private Double protein;
    private Double fat;
    private Double calories;
}


