package com.yumyumcoach.domain.diet.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Foods {

    private Long id;
    private String name;
    private Double carbohydrate;
    private Double protein;
    private Double fat;
    private Double calories;

    @Builder
    public Foods(String name, Double carbohydrate, Double protein, Double fat, Double calories) {
        this.name = name;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.calories = calories;
    }
}
